package com.numeron.brick;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public final class ModelFactory {

    private final ApiFactory apiFactory;

    private static ModelFactory modelFactory = new ModelFactory(null);

    private ModelFactory(Object iRetrofit) {
        this.apiFactory = ApiFactory.create(iRetrofit);
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
    static void install(Object instance) {
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
    static synchronized <M> M create(Class<M> clazz, Object iRetrofit) {
        ApiFactory tempApiFactory = ApiFactory.create(iRetrofit);
        return modelFactory.createModel(clazz, tempApiFactory);
    }

    private <M> M createModel(Class<M> modelClazz, IRetrofit apiFactory) {
        try {
            //获取可用的构造器
            Constructor<M> constructor = findConstructor(modelClazz, apiFactory != null);
            if (isEmptyArguments(constructor)) {
                //如果是无参的构造器，则直接创建对象
                return constructor.newInstance();
            } else {
                //创建构造器需要的实例
                Object[] instances = generateApiInstance(constructor, apiFactory);
                //创建并返回Model对象
                return constructor.newInstance(instances);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建Retrofit Api时发生了错误！", e);
        }
    }

    /**
     * 通过构造器中的参数来创建Retrofit Api的实例
     *
     * @param constructor Model层的构造器
     * @return Retrofit Api的实例数组
     */
    private Object[] generateApiInstance(Constructor<?> constructor, IRetrofit iRetrofit) {
        if (iRetrofit != null) {
            return Util.map(constructor.getParameterTypes(), iRetrofit::create);
        } else if (apiFactory != null) {
            return Util.map(constructor.getParameterTypes(), apiFactory::create);
        } else {
            throw new RuntimeException(constructor.getDeclaringClass() + "无法实例化！");
        }
    }

    private <M> Constructor<M> findConstructor(Class<M> clazz, boolean hasRetrofit) {
        //找出可用的构造方法，其中包含无参的构造方法
        List<Constructor<?>> validConstructors = Util.filter(clazz.getConstructors(), this::allIsInterface);
        if (validConstructors.isEmpty()) throw new RuntimeException(clazz + "中没有定义满足条件的构造方法！");
        Collections.sort(validConstructors, (c1, c2) -> c1.getParameterTypes().length - c2.getParameterTypes().length);
        //取出无参的构造方法，当ApiFactory不可用时，或没有可注入的构造方法时，使用此构造方法
        final Constructor<?> emptyArgConstructor = Util.find(validConstructors, this::isEmptyArguments);
        //取出参数最多、可以注入的构造方法，可能为null
        final Constructor<?> apiArgConstructor = findArgumentLongestConstructor(validConstructors, emptyArgConstructor);
        if (apiArgConstructor != null && (hasRetrofit || apiFactory != null && apiFactory.isInitialized())) {
            //如果拥有需要注入的构造方法，并且ApiFactory已初始化，则使用有参的构造方法
            return (Constructor<M>) apiArgConstructor;
        } else if (emptyArgConstructor != null) {
            //否则使用无参的构造方法
            return (Constructor<M>) emptyArgConstructor;
        } else {
            throw new RuntimeException(clazz + "中没有无参的构造方法！");
        }
    }

    private Constructor<?> findArgumentLongestConstructor(List<Constructor<?>> validConstructors, Constructor<?> emptyArgConstructor) {
        Constructor<?> constructor = validConstructors.get(validConstructors.size() - 1);
        if (constructor != emptyArgConstructor) {
            return constructor;
        } else {
            return null;
        }
    }

    private boolean isEmptyArguments(Constructor<?> constructor) {
        return constructor.getParameterTypes().length == 0;
    }

    private boolean allIsInterface(Constructor<?> constructors) {
        return Util.all(constructors.getParameterTypes(), Class::isInterface);
    }

}
