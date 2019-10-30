package com.numeron.brick

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class ViewModel : androidx.lifecycle.ViewModel(), CoroutineScope by MainScope() {

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

}
