package com.numeron.util

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@Suppress("UNCHECKED_CAST")
class ActivityExtraDelegate<T>(private val defaultValue: T) : ReadOnlyProperty<Activity, T> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return thisRef.intent.extras!!.read(property.name, defaultValue)
    }
}