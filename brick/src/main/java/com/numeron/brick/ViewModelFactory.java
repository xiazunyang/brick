package com.numeron.brick;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;

@SuppressWarnings("unchecked")
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Object[] arguments;

    public ViewModelFactory(Object... arguments) {
        this.arguments = arguments;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Constructor<?>[] constructors = modelClass.getConstructors();
        Constructor<?> constructor = Util.find(constructors,
                element -> element.getParameterTypes().length == arguments.length);
        if (constructor == null)
            throw new RuntimeException(this + " constructor arguments do not match the " + modelClass + " constructor arguments.");
        try {
            return (T) constructor.newInstance(arguments);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }

}
