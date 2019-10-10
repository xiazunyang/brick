package com.numeron.brick;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public final class ModelFactory {

    private final ApiFactory apiFactory;

    private static ModelFactory modelFactory = new ModelFactory(null);

    private ModelFactory(ApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    /**
     * 调用此方法来初始化，需要传入Retrofit对象，或者是有与create方法同名、同签名的方法的其它对象。
     *
     * @param instance 建议是Retrofit
     *                 如果对Retrofit进行了二次封装，建议实现 {@link IRetrofit}接口后传入此方法
     * @see IRetrofit
     * @see retrofit2.Retrofit#create(Class)
     */
    @SuppressWarnings("JavadocReference")
    public synchronized static void install(@NotNull Object instance) {
        ApiFactory apiFactory = ApiFactory.create(instance);
        modelFactory = new ModelFactory(apiFactory);
    }

    /**
     * 通过ViewModel的实例，来创建它泛型参数中指定的Model对象。
     *
     * @param viewModel 继承自AbstractViewModel的实例。
     * @param iRetrofit 用来创建Retrofit Api的一次性工具类。
     * @return Model层实例。
     */
    @NotNull
    public static synchronized <M, VM extends IViewModel> M create(@NotNull VM viewModel, Object iRetrofit) {
        ApiFactory tempApiFactory = ApiFactory.create(iRetrofit);
        return modelFactory.createModel(viewModel.getClass(), tempApiFactory);
    }

    @NotNull
    private <M, VM extends IViewModel> M createModel(Class<VM> viewModelClazz, IRetrofit apiFactory) {
        try {
            //从ViewModel的实现类的Class中获取Model的Class
            Class<M> modelClazz = findModelClass(viewModelClazz);
            //获取可用的构造器
            Constructor<M> constructor = findConstructor(modelClazz);
            //创建构造器需要的实例
            Object[] instances = generateApiInstance(constructor, apiFactory);
            //创建并返回Model对象
            return constructor.newInstance(instances);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建Retrofit Api时发生了错误！");
        }
    }

    /**
     * 通过构造器中的参数来创建Retrofit Api的实例
     *
     * @param constructor Model层的构造器
     * @return Retrofit Api的实例数组
     */
    private <M> Object[] generateApiInstance(Constructor<M> constructor, IRetrofit iRetrofit) {
        if (iRetrofit != null) {
            return ArrayUtil.map(constructor.getParameterTypes(), iRetrofit::create);
        } else {
            return ArrayUtil.map(constructor.getParameterTypes(), apiFactory::create);
        }
    }

    private <M> Constructor<M> findConstructor(Class<M> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (apiFactory.isInitialized()) {
            return (Constructor<M>) ArrayUtil.find(constructors, this::allIsInterface);
        } else {
            return (Constructor<M>) ArrayUtil.find(constructors, this::isEmptyArguments);
        }
    }

    private boolean isEmptyArguments(Constructor<?> constructor) {
        return constructor.getParameterTypes().length == 0;
    }

    private boolean allIsInterface(Constructor<?> constructors) {
        return ArrayUtil.all(constructors.getParameterTypes(), Class::isInterface);
    }

    private <M> Class<M> findModelClass(@NotNull Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == AbstractViewModel.class) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            return findModelClass(genericSuperclass);
        }
        try {
            if (superClass == com.numeron.brick.coroutine.AbstractViewModel.class) {
                Type genericSuperclass = clazz.getGenericSuperclass();
                return findModelClass(genericSuperclass);
            }
        } catch (Throwable ignored) {
        }
        if (superClass != null) {
            return findModelClass(superClass);
        }
        throw new RuntimeException(clazz + "没有继承自AbstractViewModel！");
    }

    private <M> Class<M> findModelClass(Type abstractViewModelType) {
        ParameterizedType parameterizedType = (ParameterizedType) abstractViewModelType;
        Type secondActualType = parameterizedType.getActualTypeArguments()[1];
        return (Class<M>) secondActualType;
    }

}
