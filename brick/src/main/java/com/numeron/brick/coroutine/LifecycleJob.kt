package com.numeron.brick.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Job

internal class LifecycleJob(
        job: Job,
        lifecycleOwner: LifecycleOwner,
        until: Lifecycle.Event) : Job by job {

    init {
        val lifecycle = lifecycleOwner.lifecycle
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            val lifecycleJobObserver = LifecycleJobObserver(job, until)
            lifecycle.addObserver(lifecycleJobObserver)
            //在协程结束时，移除监听生命周期
            invokeOnCompletion {
                lifecycle.removeObserver(lifecycleJobObserver)
            }
        } else {
            cancel()
        }
    }

    private class LifecycleJobObserver(
            private val job: Job,
            private val until: Lifecycle.Event) : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event >= until && job.isActive) {
                job.cancel()
                source.lifecycle.removeObserver(this)
            }
        }
    }

}