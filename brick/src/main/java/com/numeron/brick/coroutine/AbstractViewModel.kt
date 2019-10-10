package com.numeron.brick.coroutine

import androidx.lifecycle.ViewModel
import com.numeron.brick.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

abstract class AbstractViewModel<V : IView, M : Any> : ViewModel(), IViewModel<V, M>, CoroutineScope by MainScope() {

    private lateinit var view: V
    private lateinit var model: M

    final override fun getView(): V {
        return view
    }

    final override fun getModel(): M {
        return model
    }

    final override fun onCreated(view: V, iRetrofit: Any?) {
        if (!this::view.isInitialized) {
            this.view = view
        }
        if (!this::model.isInitialized) {
            this.model = ModelFactory.create(this, iRetrofit)
        }
    }

}
