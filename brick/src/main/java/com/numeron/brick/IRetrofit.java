package com.numeron.brick;

import androidx.annotation.NonNull;

/**
 * Retrofit中并没有提供相应的接口，所以此接口是对Retrofit中的create方法的代理
 * 当项目中使用了二次封装的Retrofit工具类，让工具类实现此接口，在实现方法中调用Retrofit实际的create方法
 * 如果你不知道怎么做，你可以浏览此页面来查看示例：
 * https://github.com/xiazunyang/numeron/blob/master/app/src/main/java/com/numeron/wan/util/Http.kt#L19
 *
 * @see retrofit2.Retrofit#create(Class)
 */
@SuppressWarnings("JavadocReference")
public interface IRetrofit {

    <T> T create(@NonNull Class<T> clazz);

}