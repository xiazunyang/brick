package com.numeron.brick;

import androidx.lifecycle.ViewModel;

public abstract class AbstractViewModel<V extends IView, M> extends ViewModel implements IViewModel<V, M> {

    private V view;
    private M model;

    @Override
    final public V getView() {
        return view;
    }

    @Override
    final public M getModel() {
        return model;
    }

    @Override
    final public void onCreated(V view, Object iRetrofit) {
        if (this.view == null) {
            this.view = view;
        }
        if (this.model == null) {
            this.model = Brick.createModel(Brick.fetchModelClass(this), iRetrofit);
        }
    }

}
