###### 此项目是我个人在工作(Android开发)中不断总结、抽象而来，囊括了架构、网络、线程、UI等，每个功能都单独整理到一个模块之中，可以单独引用。看得上哪个模块，按需自取，更欢迎不吝赐教。       
  

### 模块功能介绍

* app
    * 示例工程，使用了以下所有的模块以及Kotlin协程、Room和Paging，极少的代码即可实现【Network->Database->UI】的软件架构。
* brick
    * 兼容纯Java项目。
    * 利用ViewModel实现的、支持Kotlin协程的MVP、MVVM架构。
    * 模块内提供ViewModel抽象类，是原ViewModel的子类，可以在ViewModel中直接启动协程，协程拥有与ViewModel一致的生命周期。[查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/contract/ArticleListContract.kt#L55)
    * 提供Kotlin扩展方法和静态工厂方法来创建ViewModel，可以向ViewModel的实例中传入参数。[使用示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/activity/ArticleListActivity.kt#L36)
    * 在创建Model时，可以通过构造方法注入Retrofit Api接口实例和Room Dao的接口实例，并且可以通过Kotlin代理特性，精简Model类中的代码。查看[使用示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/contract/ArticleListContract.kt#L29)
* http
    * 兼容纯Java项目。
    * HTTP网络工具，是对Retrofit2.6.1的再封装，请在自己的工程中实现AbstractHttpUtil抽象类([查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/other/Http.kt))。
    * AbstractHttpUtil中默认已集成下载文件的拦截器和转换器，当Retrofit Api的方法里面有用Tag注解标记的File类型的参数、并且返回结果指定为Response<ResponseBody>或ResponseBody或File类型时，作为下载请求来处理。[查看示例](https://juejin.im/post/5dc68e61f265da4d2125dc6d)
    * 提供常用的Date转换器和反序列化工具。
* adapter
    * RecyclerView.Adapter辅助工具，完成Adapter仅需实现1个方法([查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/activity/WeChatAuthorActivity.kt#L39))。
    * 另外提供列表差异对比并自动处理动画的工具。
* chameleon
    * 可以动态切换主题颜色的工具，非常轻量级，侵入性低，切换时无卡顿。  
    ![image](https://github.com/xiazunyang/brick/blob/master/chameleon/demo.gif)
* result
    * 能够精简在申请权限时和Activity之间传递参数和回传参数的代码。
* rx
    * 当使用RxJava时，让数据流与ViewModel的生命周期绑定，数据流将在ViewModel被弃用时停止订阅。
* context-util
    * 提供全局获取Context和Application的扩展方法、dp/sp计算的相关扩展方法以及Toast的相关扩展方法。需要通过initContext方法进行初始化。
* delegate
    * 提供属性代理的工具类。
    * SharedPreferences属性读写代理。 [查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/other/Preferences.kt)
    * Activity intent extra属性只读代理。[查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/activity/ArticleListActivity.kt#L33)
    * Fragment arguments bundle属性只读代理。
* starter
    * start系列的扩展方法以及Intent相关的扩展方法[查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/activity/ArticleListActivity.kt#L27)
* stateful-layout
    * 用于切换状态的布局，可以自定义各种状态下要显示的View，也可以自定义切换时的动画效果。
    * [查看xml布局示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/res/layout/activity_article_list_layout.xml#L8)
    * [查看更改状态示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/activity/ArticleListActivity.kt#L52)
* stateful-livedata
    * 提供一个在多线程的环境中方便向UI线程发送数据状态的LiveData。[查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/contract/ArticleListContract.kt#L58)
    * 配合stateful-layout模块食用，风味更佳！[查看示例](https://github.com/xiazunyang/brick/blob/master/app/src/main/java/com/numeron/wandroid/activity/ArticleListActivity.kt#L52)
    
### 在自己的项目中使用

* 在你的项目的根目录下的build.gradle文件中添加以下代码，如果已存在则忽略
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```  
* 在你的子模块的build.gradle文件中添加以下依赖：  

模块|依赖
---|---
adapter|implementation 'com.github.xiazunyang.brick:adapter:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
brick|implementation 'com.github.xiazunyang.brick:brick:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
chameleon|implementation 'com.github.xiazunyang.brick:chameleon:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
context-util|implementation 'com.github.xiazunyang.brick:context-util:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
delegate|implementation 'com.github.xiazunyang.brick:delegate:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
http|implementation 'com.github.xiazunyang.brick:http:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
result|implementation 'com.github.xiazunyang.brick:result:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
rx|implementation 'com.github.xiazunyang.brick:rx:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
stateful-layout|implementation 'com.github.xiazunyang.brick:stateful-layout:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
stateful-livedata|implementation 'com.github.xiazunyang.brick:stateful-livedata:![jitpack](https://jitpack.io/v/xiazunyang/brick.svg)'
