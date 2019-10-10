@file:JvmName("KotlinExpansion")

package com.numeon.brick

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders


private val IView.viewModelProvider
    get() = when (this) {
        is FragmentActivity -> ViewModelProviders.of(this)
        is Fragment -> ViewModelProviders.of(this)
        else -> throw IllegalArgumentException()
    }


@JvmOverloads
fun <VM, V : IView> V.createViewModel(clazz: Class<VM>, iRetrofit: Any? = null): VM where VM : IViewModel<V, *>, VM : ViewModel {
    val viewModel = viewModelProvider.get(clazz)
    viewModel.onCreated(this, iRetrofit)
    return viewModel
}


inline fun <reified VM, V : IView> V.createViewModel(iRetrofit: Any? = null): VM where VM : IViewModel<V, *>, VM : ViewModel {
    return createViewModel(VM::class.java, iRetrofit)
}