package com.numeron.rx

import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


open class ResultObserver<T>(
        protected val owner: LifecycleOwner,
        protected val callback: (Result<T>) -> Unit
) : Observer<T> {

    override fun onComplete() = Unit

    override fun onSubscribe(d: Disposable) {
        LifecycleDispose(owner).accept(d)
    }

    override fun onNext(t: T) {
        callback(Result.success(t))
    }

    override fun onError(e: Throwable) {
        callback(Result.failure(e))
    }

}