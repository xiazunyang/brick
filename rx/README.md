### RxJava与MVP、MVVM一起使用的辅助工具

* LifecycleDispose
    * 调用doOnSubscribe()方法时传入或在onSubscribe()方法中调用。
    * 当View的生命周期与until一致并且Disposable未终止运行时，会产生终止动作，避免内存泄漏或空指针。
    
* ViewAttachStateDispose
    * 当拿不到View层的生命周期时，也可以使用android.view.View来获取View层的状态。
    * 当传入的android.view.View对象从窗口分离时产生终止动作，避免内存泄漏或空指针。
    
* ResultObserver
    * Observable的观察者，当结果被封装到Result中时，可以使用此类。
    * LifecycleDispose适用。
    * 当onNext方法或onError方法运行时，会调用callback: (Result<T>) -> Unit。
    
* LoadingResultObserver
    * 继承自ResultObserver，拥有其特性。
    * 会在开始时通知View层显示等待框，并在结束时通知View层关闭等待框。