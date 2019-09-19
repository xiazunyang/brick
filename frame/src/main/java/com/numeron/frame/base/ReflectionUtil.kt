package com.numeron.frame.base

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @return true表示@receiver是[targetClass]的子类，false则不是
 */
fun Type.isSubclass(targetClass: Class<*>): Boolean {
    if (this is Class<*>) {
        var clazz: Class<*>? = this
        while (clazz != null) {
            val superclass = clazz.superclass
            if (superclass == targetClass) {
                return true
            }
            if (clazz.interfaces.contains(targetClass)) {
                return true
            }
            clazz = superclass
        }
    }
    return false
}

/**
 * 获取Class中所有指定了泛型的父类
 */
val Class<*>.allGenericSuperclass: List<Class<*>>
    get() {
        val list = mutableListOf<Class<*>>()
        var clazz: Class<*>? = this
        while (clazz != null) {
            val superclass = clazz.genericSuperclass
            if (superclass is ParameterizedType) {
                list.add(clazz)
            }
            clazz = clazz.superclass
        }
        return list
    }