@file:JvmName("RxClear")

package com.numeron.rx

import androidx.lifecycle.ViewModel

import org.reactivestreams.Publisher

import java.io.Closeable

import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeOnSubscribe
import io.reactivex.MaybeSource
import io.reactivex.MaybeTransformer
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.disposables.Disposable


private val getTagMethod by lazy {
    ViewModel::class.java.getDeclaredMethod("getTag", String::class.java).also {
        it.isAccessible = true
    }
}

private val setTagIfAbsentMethod by lazy {
    ViewModel::class.java.getDeclaredMethod("setTagIfAbsent", String::class.java, Any::class.java).also {
        it.isAccessible = true
    }
}

private const val SIGNAL_TAG = "com.numeron.branch.ViewModel.CLEARED_SIGNAL"

private fun ViewModel.getClearedSignal(): OnClearedSubscribe {
    try {
        val tag = getTagMethod.invoke(this, SIGNAL_TAG)
        if (tag != null) {
            return tag as OnClearedSubscribe
        }
        val subscribe = OnClearedSubscribe()
        setTagIfAbsentMethod.invoke(this, SIGNAL_TAG, subscribe)
        return subscribe
    } catch (throwable: Throwable) {
        throw RuntimeException("绑定" + this + "的生命周期时发生了错误：", throwable)
    }
}

fun <T> ViewModel.bind(): Transformer<T> {
    return Transformer(getClearedSignal())
}

class OnClearedSubscribe : ObservableOnSubscribe<String>, SingleOnSubscribe<String>, MaybeOnSubscribe<String>, CompletableOnSubscribe, FlowableOnSubscribe<String>, Closeable {

    private val disposable = SimpleDisposable()
    private var observableEmitter: ObservableEmitter<String>? = null
    private var flowableEmitter: FlowableEmitter<String>? = null
    private var completableEmitter: CompletableEmitter? = null
    private var singleEmitter: SingleEmitter<String>? = null
    private var maybeEmitter: MaybeEmitter<String>? = null

    override fun subscribe(emitter: ObservableEmitter<String>) {
        this.observableEmitter = emitter
        emitter.setDisposable(disposable)
    }

    override fun subscribe(emitter: SingleEmitter<String>) {
        this.singleEmitter = emitter
        emitter.setDisposable(disposable)
    }

    override fun subscribe(emitter: CompletableEmitter) {
        this.completableEmitter = emitter
        emitter.setDisposable(disposable)
    }

    override fun subscribe(emitter: FlowableEmitter<String>) {
        this.flowableEmitter = emitter
        emitter.setDisposable(disposable)
    }

    override fun subscribe(emitter: MaybeEmitter<String>) {
        this.maybeEmitter = emitter
        emitter.setDisposable(disposable)
    }

    override fun close() {
        maybeEmitter?.onSuccess(SIGNAL_TAG)
        singleEmitter?.onSuccess(SIGNAL_TAG)
        flowableEmitter?.onNext(SIGNAL_TAG)
        observableEmitter?.onNext(SIGNAL_TAG)
        completableEmitter?.onComplete()
    }

}

class Transformer<T>(private val onClearedSubscribe: OnClearedSubscribe) : ObservableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, FlowableTransformer<T, T>, CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.takeUntil(Completable.create(onClearedSubscribe))
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.takeUntil(Flowable.create(onClearedSubscribe, BackpressureStrategy.MISSING))
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.takeUntil(Maybe.create(onClearedSubscribe))
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.takeUntil(Observable.create(onClearedSubscribe))
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.takeUntil(Single.create(onClearedSubscribe))
    }
}

private class SimpleDisposable : Disposable {

    private var isDisposed = false

    override fun dispose() {
        isDisposed = true
    }

    override fun isDisposed(): Boolean {
        return isDisposed
    }
}
