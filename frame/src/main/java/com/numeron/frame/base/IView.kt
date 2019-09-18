package com.numeron.frame.base

import androidx.lifecycle.LifecycleOwner

/**
 * MVP或MVVM模式中的V层的基类
 * 此接口继承自LifecycleOwner，供Presenter或ViewModel感知View的生命周期
 */
interface IView : LifecycleOwner {

    /**
     * 当后台线程开始运行前，调用此方法通知View层显示等待框
     * @param message String?   需要显示的等待框的消息文本
     * @param title String  需要显示的等待框标题
     * @param isCancelable Boolean  此对话框是否阻塞用户的操作
     */
    fun showLoading(message: String? = null, title: String = "请稍候", isCancelable: Boolean = true)

    /**
     * 当后台线程运行结束时，调用此方法通知View关闭等待框
     */
    fun hideLoading()

}