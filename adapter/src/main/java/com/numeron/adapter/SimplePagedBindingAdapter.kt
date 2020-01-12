package com.numeron.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.numeron.common.Identifiable
import java.lang.reflect.Method

class SimplePagedBindingAdapter<T : Identifiable<*>, B : ViewDataBinding>(
        @LayoutRes private val layoutId: Int,
        private val onItemClickListener: ((T) -> Unit)? = null
) : PagedBindingAdapter<T, B>(layoutId) {

    private lateinit var variableMethod: Method
    private lateinit var listenerMethod: Method

    private fun getVariableMethod(binding: B, item: T): Method {
        if (!::variableMethod.isInitialized) {
            val clazz = item::class.java
            variableMethod = binding::class.java.getMethod("set${clazz.simpleName}", clazz)
        }
        return variableMethod
    }

    private fun getListenerMethod(binding: B): Method? {
        if (onItemClickListener == null) return null
        if (!::listenerMethod.isInitialized) {
            listenerMethod = binding::class.java.getMethod("setOnItemClickListener", Function1::class.java)
        }
        return listenerMethod
    }

    override fun onBindViewHolder(holder: BindingHolder<B>, position: Int) {
        val item = getItem(position) ?: return
        val binding = holder.binding
        getVariableMethod(binding, item).invoke(binding, item)
        getListenerMethod(binding)?.invoke(binding, onItemClickListener)
        binding.executePendingBindings()
    }

}