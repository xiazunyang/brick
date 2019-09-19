package com.numeron.result


class LambdaHolder<T>(val onSuccess: (T) -> Unit) {
    private var onBefore: () -> Unit = {}
    private var onDefined: (T?) -> Unit = {}
    private var onCanceled: () -> Unit = {}
    private var onDenied: (List<String>) -> Unit = {}

    /**
     * 设置在startActivityForResult后，新Activity返回时，resultCode为CANCELED时的回调
     * @param callback () -> Unit
     * @return LambdaHolder<T>  返回自己，用于继续设置其它回调
     */
    fun setOnCanceledCallback(callback: () -> Unit): LambdaHolder<T> {
        onCanceled = callback
        return this
    }

    /**
     * 设置在requestPermission后，申请的权限被拒绝时的回调
     * @param callback (List<String>) -> Unit 向外提供被拒绝的权限列表
     * @return LambdaHolder<T>   返回自己，用于继续设置其它回调
     */
    fun setOnDeniedCallback(callback: (List<String>) -> Unit): LambdaHolder<T> {
        onDenied = callback
        return this
    }

    /**
     * 设置在onActivityResult后，调用其它回调之前执行的回调
     * @param callback () -> Unit
     * @return LambdaHolder<T>
     */
    fun setBeforeCallback(callback: () -> Unit): LambdaHolder<T> {
        onBefore = callback
        return this
    }

    /**
     * 设置在startActivityForResult后，新Activity返回时，resultCode为USER_DEFINED时的回调
     * @param callback () -> Unit
     * @return LambdaHolder<T>  返回自己，用于继续设置其它回调
     */
    fun setOnDefinedCallback(callback: (T?) -> Unit): LambdaHolder<T> {
        onDefined = callback
        return this
    }

    /**
     * 当申请权限被拒绝时，调用此方法
     * @param list List<String> 被拒绝的权限列表
     * @return Unit
     */
    fun onDenied(list: List<String>) = this.onDenied.invoke(list)

    /**
     * 当新Activity返回时，resultCode为USER_DEFINED时调用此方法
     * @return Unit
     */
    fun onDefined(t: T?) = this.onDefined.invoke(t)

    /**
     * 当新Activity返回时，先执行此回调
     * @return Unit
     */
    fun before() = this.onBefore.invoke()

    /**
     * 当新Activity返回时，resultCode为CANDELED时调用此方法
     * @return Unit
     */
    fun onCanceled() = this.onCanceled.invoke()

}