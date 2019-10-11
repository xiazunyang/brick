package com.numeron.brick;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
class Util {

    private Util() {
    }

    static <M> Class<M> fetchModelClass(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        Class<?>[] interfaces = superClass.getInterfaces();
        if (contains(interfaces, element -> element == IViewModel.class)) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            return getModelClass(genericSuperclass);
        }
        if (superClass != null) {
            return fetchModelClass(superClass);
        }
        throw new RuntimeException();
    }

    private static <M> Class<M> getModelClass(Type abstractViewModelType) {
        ParameterizedType parameterizedType = (ParameterizedType) abstractViewModelType;
        Type secondActualType = parameterizedType.getActualTypeArguments()[1];
        return (Class<M>) secondActualType;
    }


    static <T> T find(Iterable<T> array, Predicate<T> predicate) {
        for (T element : array) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    static <T> List<T> filter(T[] array, Predicate<T> predicate) {
        ArrayList<T> list = new ArrayList<>();
        for (T element : array) {
            if (predicate.test(element)) {
                list.add(element);
            }
        }
        return list;
    }

    static <T, R> R[] map(T[] array, Transform<T, R> transform) {
        Object[] objects = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            R mapResult = transform.transform(array[i]);
            objects[i] = mapResult;
        }
        return (R[]) objects;
    }

    static <T> boolean all(T[] array, Predicate<T> predicate) {
        for (T element : array) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    private static <T> boolean contains(T[] array, Predicate<T> predicate) {
        for (T element : array) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    interface Transform<T, R> {

        R transform(T element);

    }

    interface Predicate<T> {

        boolean test(T element);

    }

}
