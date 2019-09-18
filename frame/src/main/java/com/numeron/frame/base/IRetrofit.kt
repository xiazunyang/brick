package com.numeron.frame.base

/**
 * 自动为Model层注入Http Api实例时用于的工具类
 * 请在自己的Http Util中实现此接口
 */
interface IRetrofit {

    fun <T> create(clazz: Class<T>): T

}