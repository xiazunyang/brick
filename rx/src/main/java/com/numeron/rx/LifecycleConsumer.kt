package com.numeron.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


class LifecycleConsumer(
        lifecycle: Lifecycle,
        until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) : Consumer<Disposable> {

    private val disposableConsumerObserver = DisposableConsumerObserver(until)

    init {
        val currentState = lifecycle.currentState
        if (currentState.isAtLeast(Lifecycle.State.CREATED)) {
            lifecycle.addObserver(disposableConsumerObserver)
        }
    }

    override fun accept(disposable: Disposable) {
        disposableConsumerObserver.disposable = disposable
    }

    private class DisposableConsumerObserver(private val until: Lifecycle.Event) : LifecycleEventObserver {

        lateinit var disposable: Disposable

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event >= until && ::disposable.isInitialized && !disposable.isDisposed) {
                source.lifecycle.removeObserver(this)
                disposable.dispose()
            }
        }

    }


}