package com.numeon.brick;

import org.jetbrains.annotations.NotNull;

public interface IPresenter<V extends IView, M extends IModel> {

    @NotNull
    V getView();

    @NotNull
    M getModel();

}
