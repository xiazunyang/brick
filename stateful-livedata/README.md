### 提供一个在多线程的环境中方便向UI线程发送数据状态的LiveData。

* 在ViewModel中声明一个StatefulLiveData.
* 在Activity或Fragment中观察StatefulLiveData。
* 在后台线程中通过postLoading()、postFailure、postSuccess以及postEmpty等方法来提交数据状态。