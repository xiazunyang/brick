package com.numeron.rx

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import com.numeron.frame.base.IView
import io.reactivex.disposables.Disposable


open class LoadingResultObserver<T>(
        view: IView, private val loadingMessage: String? = null, callback: (Result<T>) -> Unit
) : ResultObserver<T>(view, callback) {

    protected val view: IView
        get() = owner as IView

    private val showLoadingRunnable = {
        if (view.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            view.showLoading(loadingMessage)
        }
    }

    override fun onComplete() {
        if (view.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            view.hideLoading()
        }
    }

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showLoadingRunnable()
        } else {
            Handler(Looper.getMainLooper()).post(showLoadingRunnable)
        }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        onComplete()
    }

}