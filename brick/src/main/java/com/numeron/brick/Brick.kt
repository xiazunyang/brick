@file:JvmName("Brick")

package com.numeron.brick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner


/**
 * 调用此方法进行初始化，需要传入Retrofit或其它包装了create方法的对象作为参数，用于创建Api实例
 */
@JvmOverloads
fun install(retrofit: Any, room: Any? = null) {
    ModelFactory.install(retrofit, room)
}

/**
 * 当使用多个数据库时，请在初始化前，使用此方法追加其它数据库
 */
fun addRoom(vararg room: Any) {
    ModelFactory.addRoom(*room)
}


/**
 * 供外部创建Model的实例的工厂方法
 *
 * @param clazz     Model的Class
 * @param iRetrofit 注入Retrofit Api时要使用的Retrofit，不传时，使用初始化时的全局Retrofit
 * @return Model    实例
 */
@JvmOverloads
fun <M> stack(clazz: Class<M>, iRetrofit: Any? = null): M {
    return ModelFactory.create(clazz, iRetrofit)
}


/**
 * 供Kotlin使用的createModel方法
 * @param iRetrofit Any? 创建Model的实例时，用于创建Retrofit Api实例的Retrofit或其它工具类
 * @return M Model的实例
 */
inline fun <reified M> stack(iRetrofit: Any? = null) = stack(M::class.java, iRetrofit)

@JvmOverloads
fun <VM : ViewModel> createViewModel(clazz: Class<VM>, store: ViewModelStore, factory: ViewModelProvider.Factory = ViewModelFactory()): VM {
    return ViewModelProvider(store, factory).get(clazz)
}


/**
 * 创建ViewModel的工厂方法
 * @receiver ViewModelStoreOwner 的扩展方法
 * @param clazz Class<VM>  要创建的ViewModel的Class对象
 * @param factory Factory   用于创建ViewModel对象的工厂
 * @return VM 创建后的实例
 */
@JvmOverloads
fun <VM : ViewModel> ViewModelStoreOwner.createViewModel(
        clazz: Class<VM>, factory: ViewModelProvider.Factory = ViewModelFactory()): VM {
    return ViewModelProvider(this, factory).get(clazz)
}

/**
 * [createViewModel] 的inline方法
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.createViewModel(factory: ViewModelProvider.Factory): VM {
    return createViewModel(VM::class.java, factory)
}

/**
 * 如果ViewModel需要接收参数，建议使用此方法创建ViewModel实例
 * @param arguments Array<out Any> ViewModel的参数
 * @return VM 创建后的实例
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.createViewModel(vararg arguments: Any): VM {
    return createViewModel(ViewModelFactory(*arguments))
}