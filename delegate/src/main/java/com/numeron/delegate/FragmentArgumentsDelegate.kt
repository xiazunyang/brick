package com.numeron.delegate

import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@Suppress("UNCHECKED_CAST")
class FragmentArgumentsDelegate<T>(private val defaultValue: T, private val key: String?) : ReadOnlyProperty<Fragment, T> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments!!.read(key ?: property.name, defaultValue)
    }
}