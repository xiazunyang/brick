package com.numeron.rx

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.numeon.brick.IView
import io.reactivex.disposables.Disposable


open class LoadingResultObserver<T>(
        view: IView, private val loadingMessage: String = "正在加载", callback: (Result<T>) -> Unit
) : ResultObserver<T>(view, callback) {

    protected val view: IView
        get() = owner as IView

    private val showLoadingRunnable = {
        //检查当前V的生命周期
        if (view.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            //确保V层的生命周期未销毁，显示等待框
            view.showLoading(loadingMessage)
            //并且添加一个生命周期的回调，确保在V层销毁之前取消显示等待框
            view.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    view.hideLoading()
                }
            })
        }
    }

    override fun onComplete() {
        //当订阅结束时并且V层的生命周期未销毁，取消显示等待框
        if (view.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            view.hideLoading()
        }
    }

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        //检查当前线程是否是主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            //是的话，显示对话框
            showLoadingRunnable()
        } else {
            //否则交给主线程来处理
            Handler(Looper.getMainLooper()).post(showLoadingRunnable)
        }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        onComplete()
    }

}