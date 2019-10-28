package com.numeron.brick;

import androidx.room.RoomDatabase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

class DaoFactory {

    private final List<Method> methods;
    private final RoomDatabase instance;

    private DaoFactory(RoomDatabase instance, List<Method> methods) {
        this.instance = instance;
        this.methods = methods;
    }

    static DaoFactory create(RoomDatabase room) {
        if (room == null) return null;
        //获取所有公开的的方法
        Method[] methods = room.getClass().getMethods();
        List<Method> methodList = Util.filter(methods, element -> {
                    Class<?> returnType = element.getReturnType();
                    return returnType.isInterface() || Modifier.isAbstract(returnType.getModifiers());
                }
        );
        return new DaoFactory(room, methodList);
    }

    @SuppressWarnings("unchecked")
    <T> T getDao(Class<T> clazz) {
        Method method = Util.find(methods, element -> element.getReturnType() == clazz);
        if (method == null)
            throw new RuntimeException("请确定" + instance + "中有返回值为" + clazz + "的抽象方法。");
        try {
            return (T) method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException("使用" + method + "创建" + clazz + "的实例时，发生了错误。", e);
        }
    }

}
