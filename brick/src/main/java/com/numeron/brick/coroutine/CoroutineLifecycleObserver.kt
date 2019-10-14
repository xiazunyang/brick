package com.numeron.brick.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

class CoroutineLifecycleObserver(
        private val coroutineScope: CoroutineScope,
        private val until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event >= until && coroutineScope.isActive) {
            coroutineScope.cancel()
            source.lifecycle.removeObserver(this)
        }
    }

}