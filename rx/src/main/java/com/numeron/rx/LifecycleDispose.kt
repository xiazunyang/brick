package com.numeron.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * 通过Lifecycle的生命周期变化，来处理RxJava的Disposable，确保不会发生内存泄漏
 * @param owner LifecycleOwner V层的实现类
 * @param until Event    要在哪个生命周期回调时终止Disposable，默认是 {@link Lifecycle.Event#ON_DESTROY}
 * @constructor
 */
open class LifecycleDispose(
        owner: LifecycleOwner,
        private val until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) : Consumer<Disposable> {

    private val lifecycle = owner.lifecycle

    override fun accept(disposable: Disposable) {
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