@file:JvmName("KotlinExpansion")

package com.numeon.brick

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders


val IView.viewModelProvider
    get() = when (this) {
        is Fragment -> ViewModelProviders.of(this)
        is FragmentActivity -> ViewModelProviders.of(this)
        else -> throw IllegalArgumentException()
    }


fun <VM : AbstractViewModel<V, *>, V : IView> V.createViewModel(clazz: Class<VM>): VM {
    val viewModel = viewModelProvider.get(clazz)
    viewModel.attachView(this)
    return viewModel
}


inline fun <reified VM : AbstractViewModel<V, *>, V: IView> V.createViewModel(): VM {
    return createViewModel(VM::class.java)
}