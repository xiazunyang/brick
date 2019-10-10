package com.numeon.brick;

import org.jetbrains.annotations.NotNull;

public interface IViewModel<V extends IView, M> {

    @NotNull
    V getView();

    @NotNull
    M getModel();

    void onCreated(@NotNull V view, Object iRetrofit);

}
