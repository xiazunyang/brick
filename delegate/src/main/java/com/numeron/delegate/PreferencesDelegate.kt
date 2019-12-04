package com.numeron.delegate

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class PreferencesDelegate<T>(
        private val sharedPreferences: SharedPreferences,
        private val defaultValue: T) : ReadWriteProperty<Any?, T> {

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return sharedPreferences.run {
            when (defaultValue) {
                is Int -> getInt(property.name, defaultValue)
                is Long -> getLong(property.name, defaultValue)
                is Float -> getFloat(property.name, defaultValue)
                is String -> getString(property.name, defaultValue)
                is Boolean -> getBoolean(property.name, defaultValue)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        } as T
    }

    @SuppressLint("ApplySharedPref")
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        sharedPreferences.edit().run {
            when (value) {
                is Int -> putInt(property.name, value)
                is Long -> putLong(property.name, value)
                is Float -> putFloat(property.name, value)
                is String -> putString(property.name, value)
                is Boolean -> putBoolean(property.name, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }.commit()
    }

}