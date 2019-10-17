package com.numeron.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable


internal class LifecycleDisposer(
        disposable: Disposable,
        lifecycle: Lifecycle,
        until: Lifecycle.Event) {

    init {
        val currentState = lifecycle.currentState
        if (currentState.isAtLeast(Lifecycle.State.CREATED)) {
            lifecycle.addObserver(DisposableLifecycleObserver(disposable, until))
        }
    }

    private class DisposableLifecycleObserver(
            private val disposable: Disposable,
            private val until: Lifecycle.Event) : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event >= until && !disposable.isDisposed) {
                source.lifecycle.removeObserver(this)
                disposable.dispose()
            }
        }

    }

}