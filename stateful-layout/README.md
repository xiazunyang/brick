### 一个实现了状态切换的布局，可以自定义各种状态下要显示的View，也可以自定义切换时的动画效果。

* 在xml中声明StatefulLayout节点，子节点中的第一个将视作为显示内容的View，在state为State.success时显示。
* 通过setState方法来切换要显示的View。
* xml中可用的属性表：

属性|作用
---|---
app:state|默认的状态
app:emptyView|空数据时要显示的布局引用
app:emptyTextView|空数据时，用于显示文本的控件ID
app:failureView|错误时要显示的布局引用
app:failureTextView|错误时，用于显示文本的控件ID
app:loadingView|加载数据时要显示的布局引用
app:loadingTextView|加载数据时，用于显示文本的控件ID
app:successView|指定显示内容的主控件，默认为第一个子视图
app:animationEnabled|是否启用过度动画，默认启用
app:enterAnimation|显示时的过度动画ID，默认为淡入
app:exitAnimation|隐藏时的过度动画ID，默认为淡出

