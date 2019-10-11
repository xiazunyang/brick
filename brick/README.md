### Brick框架

* 兼容纯Java项目。
* 1套业务代码对应1套View-ViewModel-Model体系，界面只需要实现View接口即可，一个界面可调用多套业务代码。
* 无须关心ViewModel和Model的创建过程，提供Kotlin扩展方法和静态工厂方法来创建ViewModel，并自动创建与VewModel关联的Model。
* 在创建Model时，可以通过构造方法注入Retrofit Api接口实例，并且可以通过Kotlin代理特性，实现无代码体的Model类。

### 使用方法
[点击此处](https://blog.csdn.net/xiazunyang/article/details/102470351)