@file:JvmName("Brick")

package com.numeron.brick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.room.RoomDatabase
import retrofit2.Retrofit


/**
 * 调用此方法进行初始化，需要传入Retrofit或其它包装了create方法的对象作为参数，用于创建Api实例
 */
@JvmOverloads
fun install(retrofit: Any, room: RoomDatabase? = null) {
    when (retrofit) {
        is Retrofit -> ModelFactory.install(retrofit, room)
        is IRetrofit -> ModelFactory.install(retrofit, room)
        else -> throw IllegalArgumentException("必需使用Retrofit或者IRetrofit的实例才能初始化！")
    }
}


/**
 * 供外部创建Model的实例的工厂方法
 *
 * @param clazz     Model的Class
 * @param iRetrofit 注入Retrofit Api时要使用的Retrofit，不传时，使用初始化时的全局Retrofit
 * @return Model    实例
 */
fun <M> createModel(clazz: Class<M>, iRetrofit: Any? = null): M {
    if (iRetrofit == null || iRetrofit is Retrofit || iRetrofit is IRetrofit) {
        return ModelFactory.create(clazz, iRetrofit)
    } else {
        throw IllegalArgumentException("必需使用Retrofit或者IRetrofit的实例才能创建${clazz}的对象！")
    }
}


/**
 * 供Kotlin使用的createModel方法
 * @param iRetrofit Any? 创建Model的实例时，用于创建Retrofit Api实例的Retrofit或其它工具类
 * @return M Model的实例
 */
inline fun <reified M> createModel(iRetrofit: Any? = null) = createModel(M::class.java, iRetrofit)

/**
 * 创建ViewModel的工厂方法
 * @receiver ViewModelStoreOwner 的扩展方法
 * @param clazz Class<VM>  要创建的ViewModel的Class对象
 * @param factory Factory   用于创建ViewModel对象的工厂
 * @return VM 创建后的实例
 */
fun <VM : ViewModel> ViewModelStoreOwner.createViewModel(clazz: Class<VM>, factory: ViewModelProvider.Factory): VM {
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