package com.numeon.brick;

import com.numeon.brick.coroutine.AbstractViewModel;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import kotlin.collections.ArraysKt;

@SuppressWarnings("unchecked")
public final class ModelFactory {

    private final Object instance;
    private final Method createMethod;

    private static ModelFactory factory = new ModelFactory(null, null);

    private ModelFactory(Object instance, Method createMethod) {
        this.createMethod = createMethod;
        this.instance = instance;
    }

    public synchronized static void install(@NotNull Object instance) {
        try {
            Class<?> clazz = instance.getClass();
            Method createMethod = clazz.getMethod("create", Class.class);
            Class<?> returnType = createMethod.getReturnType();
            if (returnType != Object.class) throw new RuntimeException();
            factory = new ModelFactory(instance, createMethod);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(instance + "中没有适用的create方法！");
        }
    }

    private boolean isInitialized() {
        return createMethod != null && instance != null;
    }

    @NotNull
    private <T> T create(Class<T> clazz) {
        //尝试通过反射来创建Retrofit Api
        if (isInitialized()) {
            try {
                Object instance = createMethod.invoke(this.instance, clazz);
                if (instance == null) throw new NullPointerException();
                //noinspection unchecked
                return (T) instance;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("创建Retrofit Api失败了！api class=" + clazz);
            }
        }
        //如果都没有创建成功，则说明未初始化
        throw new RuntimeException("ModelFactory没有初始化！请调用install方法进行初始化！");
    }

    /**
     * 通过ViewModel的实例，来创建它泛型参数中指定的Model对象。
     *
     * @param viewModel 继承自AbstractViewModel的实例。
     * @return Model层实例。
     */
    @NotNull
    public static synchronized <M extends IModel, VM extends IPresenter> M create(VM viewModel) {
        return factory.createModel(viewModel.getClass());
    }

    @NotNull
    private <M extends IModel, VM extends IPresenter> M createModel(Class<VM> viewModelClazz) {
        try {
            //从ViewModel的实现类的Class中获取Model的Class
            Class<M> modelClazz = findModelClass(viewModelClazz);
            //获取可用的构造器
            Constructor<M> constructor = findConstructor(modelClazz);
            //创建构造器需要的实例
            Object[] instances = generateApiInstance(constructor);
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
    private <M extends IModel> Object[] generateApiInstance(Constructor<M> constructor) {
        return ArraysKt.map(constructor.getParameterTypes(), this::create).toArray();
    }

    private <M extends IModel> Constructor<M> findConstructor(Class<M> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (isInitialized()) {
            return (Constructor<M>) ArraysKt.first(constructors, this::allIsInterface);
        } else {
            return (Constructor<M>) ArraysKt.first(constructors, this::isEmptyArguments);
        }
    }

    private boolean isEmptyArguments(Constructor<?> constructor) {
        return constructor.getParameterTypes().length == 0;
    }

    private boolean allIsInterface(Constructor<?> constructors) {
        return ArraysKt.all(constructors.getParameterTypes(), Class::isInterface);
    }

    private <M extends IModel> Class<M> findModelClass(@NotNull Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == AbstractViewModel.class) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            return findModelClass(genericSuperclass);
        }
        if (superClass != null) {
            return findModelClass(superClass);
        }
        throw new RuntimeException(clazz + "没有继承自AbstractViewModel！");
    }

    private <M extends IModel> Class<M> findModelClass(Type abstractViewModelType) {
        ParameterizedType parameterizedType = (ParameterizedType) abstractViewModelType;
        Type secondActualType = parameterizedType.getActualTypeArguments()[1];
        return (Class<M>) secondActualType;
    }

}
