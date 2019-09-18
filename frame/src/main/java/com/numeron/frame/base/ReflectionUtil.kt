package com.numeron.frame.base

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
            if (clazz.interfaces.any { it == targetClass }) {
                return true
            }
            clazz = superclass
        }
    }
    return false
}