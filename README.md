###### 此项目是我个人在工作(Android开发)中不断总结、抽象而来，囊括了架构、网络、线程等，每个功能都单独整理到一个模块之中，可以单独引用。看得上哪个模块，按需自取，更欢迎不吝赐教。       

### 模块功能介绍

* app
    * 示例工程，使用了以下所有的模块以及Kotlin协程、Room和Paging，极少的代码即可实现【Network->Database->UI】的软件架构。
* brick
    * 兼容纯Java项目。
    * 利用ViewModel、LiveData等JetPack组件实现的MVP、MVVM架构。    
    * 无须关心ViewModel和Model的创建过程，提供Kotlin扩展方法和静态工厂方法来创建ViewModel，并自动创建VewModel与Model。
    * 在创建Model时，可以通过构造方法注入Retrofit Api接口实例，并且可以通过Kotlin代理特性，实现无代码体的Model类。
    * 兼容纯Java项目。
    * 查看[使用方法](https://blog.csdn.net/xiazunyang/article/details/102470351)
* http
    * 兼容纯Java项目。
    * HTTP网络工具，是对Retrofit2.6.1的再封装，请在自己的工程中实现AbstractHttpUtil抽象类([查看示例](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/util/Http.kt#L8))。
* adapter
    * RecyclerView.Adapter辅助工具，完成Adapter仅需实现1个方法([查看示例](https://github.com/xiazunyang/numeron/blob/brick/app/src/main/java/com/numeron/wan/activity/MainActivity.kt#L59))。
    * 另外提供列表差异对比并自动处理动画的工具([查看实现](https://github.com/xiazunyang/numeron/blob/master/adapter/src/main/java/com/numeron/adapter/ItemDiffCallback.kt))。
* result
    * 能够精简在申请权限时和Activity之间传递参数和回传参数的代码。
* rx
    * 当使用RxJava时，配合frame中的MVP和MVVM框架，能够感知View层的生命周期，并及时作出处理，防止内存泄漏。
* context-util
    * 提供全局获取Context和Application的扩展方法、dp/sp计算的相关扩展方法以及Toast的相关扩展方法。需要通过initContext方法进行初始化。
* delegate
    * 提供属性代理的工具类。
    * SharedPreferences属性读写代理。
    * Activity intent extra属性只读代理。
    * Fragment arguments bundle属性只读代理。
* starter
    * start系列的扩展方法以及Intent相关的扩展方法
* status-layout
    * 用于切换状态的布局，可以自定义各种状态下要显示的View，也可以自定义切换时的动画效果。
* status-livedata
    * 提供一个在多线程的环境中方便向UI线程发送数据状态的LiveData。
    * 配合status-layout模块食用，风味更佳！
    
    
<!--### 在自己的工程中使用

1. 在工程根目录下的build.gradle文件中添加以下代码，如果已存在，则忽略此步。

```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

2. 添加依赖：

模块 | 依赖
---|---
brick | implementation 'com.github.xiazunyang.numeron:brick:1.2.1'
http | implementation 'com.github.xiazunyang.numeron:http:1.2.1'
adapter | implementation 'com.github.xiazunyang.numeron:rx:1.2.1'
result | implementation 'com.github.xiazunyang.numeron:result:1.2.1'
rx | implementation 'com.github.xiazunyang.numeron:rx:1.2.1'-->