@file:JvmName("BundleReader")
@file:Suppress("UNCHECKED_CAST")

package com.numeron.util

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable


internal fun <T> Bundle.read(key: String, defaultValue: T): T {
    return when (defaultValue) {
        //基本类型
        is Int -> getInt(key, defaultValue) as T
        is Byte -> getByte(key, defaultValue) as T
        is Char -> getChar(key, defaultValue) as T
        is Long -> getLong(key, defaultValue) as T
        is Short -> getShort(key, defaultValue) as T
        is Float -> getFloat(key, defaultValue) as T
        is Double -> getDouble(key, defaultValue) as T
        //可序列化的类型
        is Bundle -> getBundle(key) as? T ?: defaultValue
        is CharSequence -> getCharSequence(key) as? T ?: defaultValue
        is Parcelable -> getParcelable<Parcelable>(key) as? T ?: defaultValue
        //基本类型的数组
        is IntArray -> getIntArray(key) as? T ?: defaultValue
        is ByteArray -> getByteArray(key) as? T ?: defaultValue
        is CharArray -> getCharArray(key) as? T ?: defaultValue
        is LongArray -> getLongArray(key) as? T ?: defaultValue
        is ShortArray -> getShortArray(key) as? T ?: defaultValue
        is FloatArray -> getFloatArray(key) as? T ?: defaultValue
        is DoubleArray -> getDoubleArray(key) as? T ?: defaultValue
        //其它类型的数组
        /*is Array<*> -> when {

            else ->
        }*/
        //列表
        /*is List<*> -> when {

            else ->
        }*/
        is Serializable -> getSerializable(key) as? T ?: defaultValue
        else -> throw IllegalArgumentException("暂时不支持代理的类型。")
    }
}