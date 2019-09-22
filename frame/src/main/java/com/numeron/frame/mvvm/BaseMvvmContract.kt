package com.numeron.frame.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.numeron.frame.base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.lang.reflect.ParameterizedType

/**
 * MVVM模式中的View层基类，持有一个ViewModel对象
 */
interface IView<out VM : AbstractViewModel<IView<VM>, IModel>> : com.numeron.frame.base.IView {

    val viewModel: VM

}

/**
 * MVVM模式中的ViewModel层基类，持有View层的接口和Model层的实例
 * 此抽象类的子类必需有且只有一个无参构造方法，否则[createViewModel]方法不能正常创建对象
 */
abstract class AbstractViewModel<out V : IView<AbstractViewModel<V, M>>, out M : IModel> : ViewModel(), CoroutineScope by MainScope() {

    lateinit var view: @UnsafeVariance V
    lateinit var model: @UnsafeVariance M

}

/**
 * 用于MVVM模式中创建ViewModel的方法
 * 同时为ViewModel的View层与Model层赋值
 * 并且当Model层的构造函数中有Http Api接口作为参数时，自动为其注入Http Api的实例
 * @param retrofit IRetrofit 创建Http Api实例的工具类，如果使用自己的HTTP工具类，请实现手动实现此接口
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified V : IView<VM>, VM : AbstractViewModel<V, M>, M : IModel> V.createViewModel(retrofit: IRetrofit? = null): VM {

    //获取View层父类带有泛型参数的Type
    val abstractViewClass = javaClass.allGenericSuperclass.first {
        it.isSubclass(IView::class.java)
    }.genericSuperclass as ParameterizedType

    //从泛型参数中获取ViewModel的Class
    val viewModelImplClass = abstractViewClass.actualTypeArguments.first {
        it.isSubclass(AbstractViewModel::class.java)
    } as Class<VM>

    //获取ViewModel层带有泛型参数的Type
    val baseViewModelClass = viewModelImplClass.genericSuperclass as ParameterizedType

    //从泛型参数中获取Model的class
    val modelImplClass = baseViewModelClass.actualTypeArguments.first {
        it.isSubclass(IModel::class.java)
    } as Class<*>

    //获取Model层的构造函数
    val constructor = modelImplClass.constructors.first()

    //获取构造函数的参数
    val parameters = constructor.parameterTypes
    val model = if (parameters.isEmpty() || retrofit == null) {
        constructor.newInstance()
    } else {
        parameters
                .map {
                    retrofit.create(it) //通过Retrofit创建Http Api的实例
                }
                .toTypedArray()
                .let(constructor::newInstance)  //将创建的实例传入构造函数来创建Model对象
    } as M  //强转为真实的类型

    //创建ViewModel的实例
    val viewModel = when (this) {
        is Fragment -> ViewModelProviders.of(this).get(viewModelImplClass)
        is FragmentActivity -> ViewModelProviders.of(this).get(viewModelImplClass)
        else -> throw IllegalArgumentException("Fragment和FragmentActivity以外的类型暂时不能支持ViewModel")
    }

    //为ViewModel的Model层赋值
    viewModel.model = model

    //为ViewModel的View层赋值
    viewModel.view = this

    //返回最终的ViewModel层实例
    return viewModel
}