package com.numeron.brick;

import java.lang.reflect.Method;

class ApiFactory implements IRetrofit {

    private final Object instance;
    private final Method createMethod;

    private ApiFactory(Object instance, Method createMethod) {
        this.instance = instance;
        this.createMethod = createMethod;
    }

    boolean isInitialized() {
        return createMethod != null && instance != null;
    }

    static ApiFactory create(Object retrofit) {
        if (retrofit == null) return null;
        try {
            Class<?> clazz = retrofit.getClass();
            Method createMethod = clazz.getMethod("create", Class.class);
            Class<?> returnType = createMethod.getReturnType();
            createMethod.setAccessible(true);
            if (returnType != Object.class) throw new RuntimeException();
            return new ApiFactory(retrofit, createMethod);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(retrofit + "中没有适用的create方法！");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz) {
        //尝试通过反射来创建Retrofit Api
        if (isInitialized()) {
            try {
                Object instance = createMethod.invoke(this.instance, clazz);
                if (instance == null) throw new NullPointerException();
                return (T) instance;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("创建Retrofit Api失败了！api class=" + clazz);
            }
        }
        //未初始化
        throw new RuntimeException("ApiFactory没有初始化！请调用ModelFactory.install()方法进行初始化！");
    }

}