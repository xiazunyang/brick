package com.numeron.stateful.livedata

import androidx.lifecycle.Observer

class StatefulObserver<T>(private val callback: StatefulCallback<T>) : Observer<Stateful<T>> {

    override fun onChanged(stateful: Stateful<T>?) {
        stateful?.onFailure(callback::onFailure)
                ?.onMessage(callback::onMessage)
                ?.onLoading(callback::onLoading)
                ?.onSuccess(callback::onSuccess)
    }

}
