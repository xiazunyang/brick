package com.numeron.rx

import android.view.View
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


open class ViewAttachStateDispose(private val view: View) : Consumer<Disposable> {
    override fun accept(disposable: Disposable) {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) = Unit
            override fun onViewDetachedFromWindow(v: View) {
                if (!disposable.isDisposed) {
                    disposable.dispose()
                    view.removeOnAttachStateChangeListener(this)
                }
            }
        })
    }
}