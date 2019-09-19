package com.numeron.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


open class LifecycleDispose(
        private val owner: LifecycleOwner,
        private val until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) : Consumer<Disposable> {

    override fun accept(disposable: Disposable) {
        val lifecycle = owner.lifecycle
        val currentState = lifecycle.currentState
        if (currentState != Lifecycle.State.DESTROYED) {
            lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == until && !disposable.isDisposed) {
                    disposable.dispose()
                }
            })
        }
    }

}