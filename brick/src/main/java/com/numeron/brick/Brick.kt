@file:JvmName("Brick")

package com.numeron.brick

import androidx.room.RoomDatabase


/**
 * 调用此方法进行初始化，需要传入Retrofit或其它包装了create方法的对象作为参数，用于创建Api实例
 */
@JvmOverloads
fun install(retrofit: Any, room: RoomDatabase? = null) {
    ModelFactory.install(retrofit, room)
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