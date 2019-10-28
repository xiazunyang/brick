package com.numeron.util

import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@Suppress("UNCHECKED_CAST")
class FragmentBundleDelegate<T>(private val defaultValue: T) : ReadOnlyProperty<Fragment, T> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments!!.read(property.name, defaultValue)
    }
}