package com.numeron.brick;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
class Util {

    private Util() {
    }

    /*static <M> Class<M> fetchModelClass(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null) throw new NullPointerException("难道你传入了一个Object对象？");
        Class<?>[] interfaces = superClass.getInterfaces();
        if (contains(interfaces, element -> element == IViewModel.class)) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            return getModelClass(genericSuperclass);
        }
        return fetchModelClass(superClass);
    }

    private static <M> Class<M> getModelClass(Type abstractViewModelType) {
        ParameterizedType parameterizedType = (ParameterizedType) abstractViewModelType;
        Type secondActualType = parameterizedType.getActualTypeArguments()[1];
        return (Class<M>) secondActualType;
    }*/

    static <T> T find(Iterable<T> array, Predicate<T> predicate) {
        for (T element : array) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    static <T> T find(T[] array, Predicate<T> predicate) {
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
        ArrayList<R> list = new ArrayList<>(array.length);
        for (T element : array) {
            R mapResult = transform.transform(element);
            list.add(mapResult);
        }
        return (R[]) list.toArray();
    }

    static <T> boolean all(T[] array, Predicate<T> predicate) {
        for (T element : array) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    static <T> boolean contains(T[] array, Predicate<T> predicate) {
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