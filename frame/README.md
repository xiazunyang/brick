### MVP、MVVM架构

* View
   
    * 请在自己的项目中，使用抽象Activity或Fragment类继承mvp或mvvm包下的IView，并实现IView中所有的方法。
    * 在此类的onCreate()方法中使用createPresenter()或createViewModel()来创建Presenter或ViewModel的实例。
    * 不要指定泛型参数，把IView中的泛型参数复制到抽象类中，让实现类去指定泛型。[查看示例](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/abs/AbsMvvmActivity.kt#L21)
    * 在View的实现类的泛型参数中传入要引用的Presenter，并让View实现最终引用的View接口。

* Presenter、ViewModel
    * 让你的Presenter继承AbstractPresenter类 、ViewModel继承AbstractViewModel类。
    * 泛型参数传入实现的View与Model。
    
* Model
    * 需要实现IModel接口。
    * 可以实现Retrofit的Api接口，并在构造函数中接收一个Api接口的实例，然后使用by关键字把接口代理给构造函数中的实例，Model就可以直接调用Api中的方法来发送网络请求。