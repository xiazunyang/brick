package com.numeron.frame.mvp

import com.numeron.frame.base.IModel
import com.numeron.frame.base.IRetrofit
import com.numeron.frame.base.isSubclass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.lang.reflect.ParameterizedType

/**
 * MVP模式下的View层的基类，持有一个Presenter对象
 */
interface IView<out P : BasePresenter<IView<P>, IModel>> : com.numeron.frame.base.IView {

    val presenter: P

}

/**
 * MVP模式下的Presenter层的基类，持有View层的接口与Model层的实例
 */
abstract class BasePresenter<out V : IView<BasePresenter<V, M>>, out M : IModel> : CoroutineScope by MainScope() {

    lateinit var view: @UnsafeVariance V
    lateinit var model: @UnsafeVariance M

}


/**
 * 用于MVP模式中创建Presenter的方法
 * 同时为Presenter的View层与Model层赋值
 * 并且当Model层的构造函数中有Http Api接口作为参数时，自动为其注入Http Api的实例
 * @param retrofit IRetrofit 创建Http Api实例的工具类，如果使用自己的HTTP工具类，请实现手动实现此接口
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified V : IView<P>, P : BasePresenter<V, M>, M : IModel> V.createPresenter(retrofit: IRetrofit): P {

    //获取View层父类带有泛型参数的Type
    val abstractViewClass = javaClass.genericSuperclass as ParameterizedType

    //从泛型参数中获取Presenter的Class
    val presenterImplClass = abstractViewClass.actualTypeArguments.first {
        it.isSubclass(BasePresenter::class.java)
    } as Class<*>

    //获取Presenter层带有泛型参数的Type
    val basePresenterClass = presenterImplClass.genericSuperclass as ParameterizedType

    //从泛型参数中获取Model的class
    val modelImplClass = basePresenterClass.actualTypeArguments.first {
        it.isSubclass(IModel::class.java)
    } as Class<*>

    //获取Model层的构造函数
    val constructor = modelImplClass.constructors.first()

    //获取构造函数的参数
    val model = constructor.parameterTypes
            .map {
                //通过Retrofit创建Http Api的实例
                retrofit.create(it)
            }
            .toTypedArray()
            .let {
                //将创建的实例传入构造函数来创建Model对象，并强转为真实的类型
                constructor.newInstance(*it) as M
            }

    val presenter = presenterImplClass.newInstance() as P


    //为Presenter的Model层赋值
    presenter.model = model

    //为Presenter的View层赋值
    presenter.view = this

    //返回最终的Presenter层实例
    return presenter
}