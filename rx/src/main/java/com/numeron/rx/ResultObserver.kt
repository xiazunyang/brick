package com.numeron.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


open class ResultObserver<T>(
        owner: LifecycleOwner,
        private val until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        protected val callback: (Result<T>) -> Unit
) : Observer<T> {

    protected val lifecycle = owner.lifecycle

    override fun onComplete() = Unit

    override fun onSubscribe(disposable: Disposable) {
        LifecycleDisposer(disposable, lifecycle, until)
    }

    override fun onNext(t: T) {
        callback(Result.success(t))
    }

    override fun onError(e: Throwable) {
        callback(Result.failure(e))
    }

}