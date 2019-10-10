@file:JvmName("KotlinExpansion")

package com.numeon.brick

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders


val IView.viewModelProvider
    get() = when (this) {
        is FragmentActivity -> ViewModelProviders.of(this)
        is Fragment -> ViewModelProviders.of(this)
        else -> throw IllegalArgumentException()
    }


fun <VM, V : IView> V.createViewModel(clazz: Class<VM>): VM where VM : IPresenter<V, *>, VM : ViewModel {
    val viewModel = viewModelProvider.get(clazz)
    viewModel.onCreated(this)
    return viewModel
}


inline fun <reified VM, V : IView> V.createViewModel(): VM where VM : IPresenter<V, *>, VM : ViewModel {
    return createViewModel(VM::class.java)
}