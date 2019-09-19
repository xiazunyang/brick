### 基于Retrofit2.6.1的网络工具

* 建议使用Kotlin的object(单例)来实现AbstractHttpUtil抽象类。
* 必需重写 `val baseUrl: String`。
* 必需重写 `val convertersFactories: Iterable<Converter.Factory>`，此属性在Retrofit将Http的请求体转换为指定类型时要用到，不重写可能无法获取正确的结果类型。
* 如果有每次发送请求都需要传递的请求头(如Token)，建议重写val header: Map<String, String>，该Map中的数据会在每次发送网络请求时都传递给服务器。
* 如果要使用RxJava等其它类型来接收返回结果，则需要重写 `val callAdapterFactories: Iterable<CallAdapter.Factory>`
* 如果需要对Retrofit进行重新构建，使用`createRetrofit(build: Retrofit.Builder.() -> Unit): Retrofit`方法来重新构建一个新的Retrofit。
* 如果需要对OkHttpClient重新构建，使用`createOkHttpClient(build: OkHttpClient.Builder.() -> Unit): OkHttpClient`方法来重新构建一个新的OkHttpClient。
* 如果需要添加https证书，重写` val certificates: Array<InputStream>`来添加。
* 如果需要添加https密码，重写`val keyStorePassword: String?`来添加。
* 如果需要添加https密钥，重写`val keyStore: InputStream?`来添加。