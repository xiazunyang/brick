package com.numeon.brick

import androidx.lifecycle.LifecycleOwner

interface IView : LifecycleOwner {

    fun showLoading(message: String = "请稍候", isCancelable: Boolean = false)

    fun hideLoading()

}
