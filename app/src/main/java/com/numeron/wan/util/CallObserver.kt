package com.numeron.wan.util

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Call

open class CallObserver<T> : Observer<Call<T>> {

    override fun onComplete() {

    }

    override fun onSubscribe(disposable: Disposable) {

    }

    override fun onNext(call: Call<T>) {

    }

    override fun onError(throwable: Throwable) {

    }

}