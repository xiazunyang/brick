package com.numeron.status

import androidx.lifecycle.Observer

class StatefulObserver<T>(private val callback: StatefulCallback<T>) : Observer<Stateful<T>> {

    override fun onChanged(stateful: Stateful<T>?) {
        stateful?.onEmpty(callback::onEmpty)
                ?.onFailure(callback::onFailure)
                ?.onMessage(callback::onMessage)
                ?.onLoading(callback::onLoading)
                ?.onSuccess(callback::onSuccess)
    }

}
