package com.numeon.brick;

import androidx.lifecycle.LifecycleOwner;

public interface IView extends LifecycleOwner {

    void showLoading();

    void hideLoading();

}
