package com.numeon.brick

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

abstract class AbstractViewModel<V : IView, M : IModel> : ViewModel(), IPresenter<V, M>, CoroutineScope by MainScope() {

    private lateinit var view: V
    private lateinit var model: M

    final override fun getView(): V {
        return view
    }

    final override fun getModel(): M {
        return model
    }

    fun attachView(view: V) {
        if (!this::view.isInitialized) {
            this.view = view
        }
        if (!this::model.isInitialized) {
            this.model = ModelFactory.create(this)
        }
    }

}
