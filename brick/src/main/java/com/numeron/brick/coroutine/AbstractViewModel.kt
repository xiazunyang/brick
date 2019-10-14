package com.numeron.brick.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.numeron.brick.*
import kotlinx.coroutines.*

/**
 * 此类实现了CoroutineScope接口，可以在此类中使用launch方法启动一个协程。
 * 并且所有此方法启动的协程会在view的生命周期事件达到[untilEvent]的时候，终止它们。
 * @property view V 持有的View
 * @property model M 持有的Model
 */
abstract class AbstractViewModel<V : IView, M : Any> : ViewModel(), IViewModel<V, M>, CoroutineScope by MainScope() {

    private lateinit var view: V
    private lateinit var model: M

    /**
     * 所有在这个类中启动的非顶层协程，会在这个生命周期的事件发生时终止运行
     * 你可以在子类中覆写此方法来重新指定一个触发停止协程运行的生命周期事件
     * 或者设置为null来禁用此功能。
     */
    protected open val untilEvent: Lifecycle.Event?
        get() = Lifecycle.Event.ON_DESTROY

    final override fun getView(): V {
        return view
    }

    final override fun getModel(): M {
        return model
    }

    final override fun onCreated(view: V, iRetrofit: Any?) {
        if (!this::view.isInitialized) {
            this.view = view
            //为协程添加生命周期回调
            val event = untilEvent
            if (event != null) {
                this.view.lifecycle.addObserver(CoroutineLifecycleObserver(this, event))
            }
        }
        if (!this::model.isInitialized) {
            this.model = createModel(fetchModelClass(), iRetrofit)
        }
    }

}
