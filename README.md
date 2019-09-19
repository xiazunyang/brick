###### 此项目是我个人在工作(Android开发)中不断总结、抽象而来，囊括了架构、网络、线程等，每个功能都单独整理到一个模块之中，可以单独引用。看得上哪个模块，按需自取，更欢迎不吝赐教。       

### 模块功能介绍

* app
    * 示例工程
* adapter
    * RecyclerView.Adapter辅助工具，完成Adapter仅需实现1个方法([查看示例](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/activity/MainActivity.kt#L55))。
    * 另外提供列表差异对比并自动处理动画的工具([查看实现](https://github.com/xiazunyang/numeron/blob/master/adapter/src/main/java/com/numeron/adapter/ItemDiffCallback.kt))。
* frame
    * MVP或MVVM架构，对外提供IView接口方便其它项目实现自己的抽象View层。
    * 当M层需要访问网络时，可以自动注入Retrofit Api，通过kotlin关键字by，M层可以省略若干行重复代码。
    * 完整示例：[抽象V层](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/abs/AbsMvvmActivity.kt#L21)、[V层](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/activity/MainActivity.kt#L20)、[VM层](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/contract/MainContract.kt#L19)、[M层](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/contract/MainContract.kt#L46)  
* http
    * HTTP网络工具，是对Retrofit2.6.1的再封装，请在自己的工程中实现AbstractHttpUtil抽象类([查看示例](https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/util/Http.kt#L8))。  
* result
    * 能够精简在申请权限时和Activity之间传递参数和回传参数的代码。  
* rx
    * 当使用RxJava时，配合frame中的MVP和MVVM框架，能够感知View层的生命周期，并及时作出处理。  
    
    
### 在自己的工程中使用

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
---------| -------------
adapter | implementation 'com.github.xiazunyang.numeron:rx:1.0.0'
frame | implementation 'com.github.xiazunyang.numeron:frame:1.0.0'
http | implementation 'com.github.xiazunyang.numeron:http:1.0.0'
result | implementation 'com.github.xiazunyang.numeron:result:1.0.0'
rx | implementation 'com.github.xiazunyang.numeron:rx:1.0.0'