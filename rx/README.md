### RxJava绑定ViewModel生命周期的辅助工具

* 在RxJava流中使用.compose操作符进行绑定，绑定后的RxJava流将在ViewModel被弃用时停止订阅。