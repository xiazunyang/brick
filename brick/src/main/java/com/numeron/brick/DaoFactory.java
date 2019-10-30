package com.numeron.brick;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

class DaoFactory {

    private final List<Method> methods;
    private final Object instance;

    private DaoFactory(Object instance, List<Method> methods) {
        this.instance = instance;
        this.methods = methods;
    }

    static DaoFactory create(Object room) {
        if (room == null) return null;
        //获取所有公开的的、返回结果是抽象类的方法
        Method[] methods = room.getClass().getMethods();
        List<Method> methodList = Util.filter(methods,
                element -> Modifier.isAbstract(element.getReturnType().getModifiers()));
        return new DaoFactory(room, methodList);
    }

    @SuppressWarnings("unchecked")
    <T> T getDao(Class<T> clazz) {
        Method method = Util.find(methods, element -> element.getReturnType() == clazz);
        if (method == null)
            throw new RuntimeException("请确定" + instance + "中有返回值为" + clazz + "的方法。");
        try {
            return (T) method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException("使用" + method + "创建" + clazz + "的实例时，发生了错误。", e);
        }
    }

}
