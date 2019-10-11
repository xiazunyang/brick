package com.numeron.brick;

public interface IViewModel<V extends IView, M> {

    V getView();

    M getModel();

    void onCreated(V view, Object iRetrofit);

}
