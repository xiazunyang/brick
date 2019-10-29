package com.numeron.brick;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;

public abstract class ViewModel extends androidx.lifecycle.ViewModel implements CoroutineScope {

    private final CoroutineScope coroutineScope = CoroutineScopeKt.MainScope();

    @Override
    protected void onCleared() {
        CoroutineScopeKt.cancel(coroutineScope, null);
        super.onCleared();
    }

    @NotNull
    @Override
    public final CoroutineContext getCoroutineContext() {
        return coroutineScope.getCoroutineContext();
    }

}
