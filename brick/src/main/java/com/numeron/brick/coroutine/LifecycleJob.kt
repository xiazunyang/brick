package com.numeron.brick.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Job

internal class LifecycleJob(
        private val job: Job,
        lifecycleOwner: LifecycleOwner,
        private val until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) : Job by job, LifecycleEventObserver {

    init {
        val lifecycle = lifecycleOwner.lifecycle
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            lifecycle.addObserver(this)
        } else {
            cancel()
        }
        //在协程结束时，移除监听生命周期
        invokeOnCompletion {
            lifecycle.removeObserver(this)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event >= until && job.isActive) {
            job.cancel()
            source.lifecycle.removeObserver(this)
        }
    }

}