package com.numeron.brick

import kotlinx.coroutines.*

/**
 * 拥有一个Dispatchers.IO的协程域
 */
abstract class ViewModel : androidx.lifecycle.ViewModel(), CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO) {

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

}
