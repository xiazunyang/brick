package com.numeon.brick.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.numeon.brick.IModel
import com.numeon.brick.IPresenter
import com.numeon.brick.IView
import com.numeon.brick.ModelFactory

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

public abstract class AbstractViewModel<V : IView, M : IModel> : ViewModel(), IPresenter<V, M>, CoroutineScope by MainScope() {

    private lateinit var view: V
    private lateinit var model: M

    final override fun getView(): V {
        return view
    }

    final override fun getModel(): M {
        return model
    }

    final override fun onCreated(view: V) {
        if (!this::view.isInitialized) {
            this.view.lifecycle.addObserver(CoroutineLifecycle())
            this.view = view
        }
        if (!this::model.isInitialized) {
            this.model = ModelFactory.create(this)
        }
    }

    private inner class CoroutineLifecycle : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                cancel()
            }
        }
    }

}
