package com.numeron.brick

import kotlinx.coroutines.*

abstract class ViewModel : androidx.lifecycle.ViewModel(),
        CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO) {

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

}
