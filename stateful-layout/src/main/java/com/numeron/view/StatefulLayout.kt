package com.numeron.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import com.numeron.common.State

class StatefulLayout @JvmOverloads constructor(c: Context, a: AttributeSet? = null, i: Int = 0) :
        FrameLayout(c, a, i) {

    private var previousState: State? = null
    private var hookLoading = false

    var state: State = State.Empty
        set(value) {
            if (value != field) {
                previousState = field
                field = value
            }
            changeView()
        }

    private val failureView
        get() = getChildAt(0)

    private val emptyView
        get() = getChildAt(1)

    private val loadingView
        get() = getChildAt(2)

    private val contentView
        get() = if (successViewId == 0) getChildAt(3) else findViewById(successViewId)

    private val defaultState: Int
    private val successViewId: Int
    private val emptyTextViewId: Int
    private val failureTextViewId: Int
    private val loadingTextViewId: Int
    private val exitAnimationId: Int
    private val enterAnimationId: Int

    private var animationEnabled: Boolean

    private lateinit var loadingOperation: (Boolean) -> Unit

    init {
        val inflater = LayoutInflater.from(c)

        val tArray = c.obtainStyledAttributes(a, R.styleable.StatefulLayout)

        successViewId = tArray.getResourceId(R.styleable.StatefulLayout_successView, 0)

        val failureResId = tArray.getResourceId(
                R.styleable.StatefulLayout_failureView,
                R.layout.state_error_layout
        )
        inflater.inflate(failureResId, this)

        val emptyResId = tArray.getResourceId(
                R.styleable.StatefulLayout_emptyView,
                R.layout.state_empty_layout
        )
        inflater.inflate(emptyResId, this)

        val loadingResId = tArray.getResourceId(
                R.styleable.StatefulLayout_loadingView,
                R.layout.state_loading_layout
        )
        inflater.inflate(loadingResId, this)

        loadingTextViewId = tArray.getResourceId(
                R.styleable.StatefulLayout_loadingTextView,
                R.id.loadingStateTextView
        )
        failureTextViewId = tArray.getResourceId(
                R.styleable.StatefulLayout_failureTextView,
                R.id.errorStateTextView
        )
        emptyTextViewId = tArray.getResourceId(
                R.styleable.StatefulLayout_emptyTextView,
                R.id.emptyStateTextView
        )

        //分配默认状态
        defaultState = tArray.getInt(R.styleable.StatefulLayout_state, 0)
        //是否启动状态切换动画
        animationEnabled = tArray.getBoolean(R.styleable.StatefulLayout_animationEnabled, true)
        exitAnimationId = tArray.getResourceId(
                R.styleable.StatefulLayout_exitAnimation,
                R.anim.anim_state_layout_exit
        )
        enterAnimationId = tArray.getResourceId(
                R.styleable.StatefulLayout_enterAnimation,
                R.anim.anim_state_layout_enter
        )

        tArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        state = State.values().first {
            it.ordinal == defaultState
        }
    }

    fun setLoadingText(text: CharSequence?) {
        findViewById<TextView>(loadingTextViewId).text = text
    }

    fun setFailureText(text: CharSequence?) {
        findViewById<TextView>(failureTextViewId).text = text
    }

    fun setEmptyText(text: CharSequence?) {
        findViewById<TextView>(emptyTextViewId).text = text
    }

    fun setOnFailureTextClickListener(l: (View) -> Unit) {
        findViewById<TextView>(failureTextViewId).setOnClickListener(l)
    }

    fun setOnFailureTextClickListener(l: OnClickListener) {
        findViewById<TextView>(failureTextViewId).setOnClickListener(l)
    }

    fun setOnEmptyTextClickListener(l: (View) -> Unit) {
        findViewById<TextView>(emptyTextViewId).setOnClickListener(l)
    }

    fun setOnEmptyTextClickListener(l: OnClickListener) {
        findViewById<TextView>(emptyTextViewId).setOnClickListener(l)
    }

    fun setLoadingOperation(operation: (Boolean) -> Unit) {
        setLoadingOperation(false, operation)
    }

    fun setLoadingOperation(hookLoading: Boolean, operation: (Boolean) -> Unit) {
        if (this.hookLoading != hookLoading) {
            this.hookLoading = hookLoading
        }
        loadingOperation = operation
    }

    private fun changeView() {
        if (::loadingOperation.isInitialized) {
            loadingOperation(state == State.Loading)
        }
        if (!hookLoading && state == State.Loading) loadingView?.show() else loadingView?.hide()
        if (state == State.Success) contentView?.show() else contentView?.hide()
        if (state == State.Failure) failureView?.show() else failureView?.hide()
        if (state == State.Empty) emptyView?.show() else emptyView?.hide()
//        Log.e("StatefulLayout", "state=$state")
//        Log.e("StatefulLayout", "loadingView=${loadingView.visibility}")
//        Log.e("StatefulLayout", "emptyView=${emptyView.visibility}")
//        Log.e("StatefulLayout", "failureView=${failureView.visibility}")
//        Log.e("StatefulLayout", "contentView=${contentView.visibility}")
    }

    private fun View.show() {
        if (visibility != VISIBLE) {
            visibility = VISIBLE
            if (animationEnabled) {
                clearAnimation()
                startAnimation(AnimationUtils.loadAnimation(context, enterAnimationId))
            }
        }
    }

    private fun View.hide() {
        if (visibility != GONE) {
            visibility = GONE
            if (animationEnabled) {
                clearAnimation()
                val animation = AnimationUtils.loadAnimation(context, exitAnimationId)
                startAnimation(animation)
            }
        }
    }

}