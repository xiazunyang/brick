package com.numeron.brick;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractViewModel<V extends IView, M> extends ViewModel implements IViewModel<V, M> {

    private V view;
    private M model;

    @NotNull
    @Override
    final public V getView() {
        return view;
    }

    @NotNull
    @Override
    final public M getModel() {
        return model;
    }

    @Override
    public void onCreated(@NotNull V view, Object iRetrofit) {
        if (this.view == null) {
            this.view = view;
        }
        if (this.model == null) {
            this.model = ModelFactory.create(this, iRetrofit);
        }
    }

}
