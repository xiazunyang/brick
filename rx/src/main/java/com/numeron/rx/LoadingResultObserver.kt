package com.numeron.rx

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import io.reactivex.disposables.Disposable


open class LoadingResultObserver<T>(
        private val view: IView,
        private val message: String = "正在加载",
        private val isCancelable: Boolean = false,
        until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        callback: (Result<T>) -> Unit
) : ResultObserver<T>(view, until, callback) {

    override fun onComplete() = view.hideLoading()

    override fun onSubscribe(disposable: Disposable) {
        super.onSubscribe(disposable)
        val showLoadingRunnable = {
            view.showLoading(message, isCancelable)
        }
        //检查当前线程是否是主线程
        val mainLooper = Looper.getMainLooper()
        if (Looper.myLooper() == mainLooper) {
            //是的话，显示对话框
            showLoadingRunnable()
        } else {
            //否则交给主线程来处理
            Handler(mainLooper).post(showLoadingRunnable)
        }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        onComplete()
    }

}