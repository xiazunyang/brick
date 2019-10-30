package com.numeron.brick;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
class Util {

    private Util() {
    }

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