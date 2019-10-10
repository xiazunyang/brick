package com.numeron.brick;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

class ArrayUtil {

    private ArrayUtil() {
    }

    static <T> T find(@NotNull T[] array, @NotNull Predicate<T> predicate) {
        for (T element : array) {
            if (predicate.test(element)) {
                return element;
            }
        }
        throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    static <T, R> R[] map(@NotNull T[] array, @NotNull Transform<T, R> transform) {
        Object[] objects = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            R mapResult = transform.transform(array[i]);
            objects[i] = mapResult;
        }
        return (R[]) objects;
    }

    static <T> boolean all(@NotNull T[] array, @NotNull Predicate<T> predicate) {
        for (T element : array) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    interface Transform<T, R> {

        R transform(T element);

    }

    interface Predicate<T> {

        boolean test(T element);

    }

}
