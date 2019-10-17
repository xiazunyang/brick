@file:JvmName("Brick")

package com.numeron.brick

import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.lang.RuntimeException

/**
 * 调用此方法进行初始化，需要传入Retrofit或其它包装了create方法的对象作为参数，用于创建Api实例
 */
fun installRetrofit(retrofit: Any) {
    ModelFactory.install(retrofit)
}


/**
 * 供外部创建Model的实例的工厂方法
 *
 * @param clazz     Model的Class
 * @param iRetrofit 注入Retrofit Api时要使用的Retrofit，不传时，使用初始化时的全局Retrofit
 * @return Model    实例
 */
fun <M> createModel(clazz: Class<M>, iRetrofit: Any? = null): M {
    return ModelFactory.create(clazz, iRetrofit)
}


/**
 * 供Kotlin使用的createModel方法
 * @param iRetrofit Any? 创建Model的实例时，用于创建Retrofit Api实例的Retrofit或其它工具类
 * @return M Model的实例
 */
inline fun <reified M> createModel(iRetrofit: Any? = null) = createModel(M::class.java, iRetrofit)


/**
 * 供本框架中内部调用的方法，用于从ViewModel实例中取出与之关联的Model的Class
 * @receiver VM ViewModel实例
 * @return Class<M> 与ViewModel关联的Model的Class
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun <VM : IViewModel<*, M>, M> VM.fetchModelClass(): Class<M> {
    return Util.fetchModelClass(javaClass)
}


private val IView.viewModelProvider
    get() = when (this) {
        is FragmentActivity -> ViewModelProviders.of(this)
        is Fragment -> ViewModelProviders.of(this)
        else -> throw RuntimeException()
    }

/**
 * 创建或者获取ViewModel实例
 * @receiver V 继承了{@link IVew}接口的View，也是一个接口
 * @param clazz Class<VM>   要创建的ViewModel的Class
 * @param iRetrofit Any?    创建与ViewModel关联的Model时，如果要注入Retrofit Api，
 *          但是不能用默认的Retrofit，就新另外构建一个Retrofit传进来。也可以不传
 * @return VM   创建的ViewModel对象
 *          每个ViewModel对象会与View关联起来，View销毁前，多次调用此方法获取的都会是同一个对象
 */
@JvmOverloads
fun <VM, V : IView> V.createViewModel(clazz: Class<VM>, iRetrofit: Any? = null): VM where VM : IViewModel<V, *>, VM : ViewModel {
    val viewModel = viewModelProvider.get(clazz)
    viewModel.onCreated(this, iRetrofit)
    return viewModel
}


/**
 * 供Kotlin调用的inline方法
 * @see createViewModel
 */
inline fun <reified VM, V : IView> V.createViewModel(iRetrofit: Any? = null): VM where VM : IViewModel<V, *>, VM : ViewModel {
    return createViewModel(VM::class.java, iRetrofit)
}