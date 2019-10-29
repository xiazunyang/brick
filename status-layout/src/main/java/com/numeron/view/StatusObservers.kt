package com.numeron.view

import androidx.lifecycle.Observer
import com.numeron.common.Status

class StatusObserver(private val statusLayout: StatusLayout) : Observer<Status> {

    override fun onChanged(status: Status) {
        statusLayout.status = status
    }

}


class StatusMessageObserver(private val statusLayout: StatusLayout) : Observer<Pair<Status, String>> {
    override fun onChanged(pair: Pair<Status, String>) {
        val (status, message) = pair
        statusLayout.status = status
        when (status) {
            Status.Empty -> statusLayout.setEmptyText(message)
            Status.Loading -> statusLayout.setLoadingText(message)
            Status.Failure -> statusLayout.setFailureText(message)
            Status.Success -> Unit
        }
    }
}