@file:JvmName("CoroutineHelper")

package com.numeron.brick.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.numeron.brick.IView
import kotlinx.coroutines.*

/**
 * 将一个协程任务与Lifecycle关联起来，当Lifecycle的生命周期事件超过[until]时，将终止协程任务。
 * @receiver Job    Job的扩展方法
 * @param owner LifecycleOwner  生命周期所有者
 * @param until Event   截止的生命周期事件
 * @return Job 实现了Job接口、并代理给{@receiver}的对象
 */
fun Job.asLifecycleJob(
        owner: LifecycleOwner,
        until: Lifecycle.Event = Lifecycle.Event.ON_DESTROY): Job = LifecycleJob(this, owner, until)

/**
 * 用于启用一个协程，本质上是launch，封装了显示、关闭等待动画的操作，用于简化代码
 * @receiver VM 可以在coroutine包下的AbstractViewModel的子类中使用
 * @param message String 等待时显示的文字
 * @param isCancelable Boolean  是否允许在等待时关闭动画
 * @param task 要在协程中执行的代码
 * @return Job launch方法创建的Job
 */
fun <VM : AbstractViewModel<V, *>, V : IView> VM.loading(
        message: String = "正在加载",
        isCancelable: Boolean = false,
        task: suspend CoroutineScope.() -> Unit): Job {
    view.showLoading(message, isCancelable)
    return launch(Dispatchers.IO) {
        try {
            task()
        } finally {
            withContext(Dispatchers.Main) {
                view.hideLoading()
            }
        }
    }
}