package com.numeron.brick;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

final class OnClearedSubscribe implements ObservableOnSubscribe<String> {

    private static final String CLEARED_SIGNAL = "CLEARED_SIGNAL";
    private ObservableEmitter<String> emitter;

    @Override
    public void subscribe(ObservableEmitter<String> emitter) {
        this.emitter = emitter;
        emitter.setDisposable(new SimpleDisposable());
    }

    void onCleared() {
        if (emitter != null) {
            emitter.onNext(CLEARED_SIGNAL);
        }
    }

    final class SimpleDisposable implements Disposable {

        private boolean isDispose = false;

        @Override
        public void dispose() {
            isDispose = true;
        }

        @Override
        public boolean isDisposed() {
            return isDispose;
        }

    }

}
