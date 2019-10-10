package com.numeon.brick;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractViewModel<V extends IView, M extends IModel> extends ViewModel implements IPresenter<V, M> {

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
    final public void onCreated(V view) {
        if (this.view == null) {
            this.view = view;
        }
        if (this.model == null) {
            this.model = ModelFactory.create(this);
        }
    }

}
