package com.numeron.view

import android.content.Context
import android.util.AttributeSet
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

    /**
     * 设置状态，设置空时，将恢复上一个状态
     */
    var state: State? = null
        //当设置状态时，将状态压入栈中
        set(value) {
            if (value == null && previousState != null) {
                field = previousState
                previousState = null
                changeView()
            } else if (value != field) {
                previousState = field
                field = value
                changeView()
            }
        }

    private val failureView
        get() = getChildAt(0)

    private val emptyView
        get() = getChildAt(1)

    private val loadingView
        get() = getChildAt(2)

    private val contentView
        get() = if (contentViewId == 0) getChildAt(3) else findViewById(contentViewId)

    private val contentViewId: Int
    private val errorTextViewId: Int
    private val emptyTextViewId: Int
    private val loadingTextViewId: Int

    private val defaultStatus: Int
    private val exitAnimationId: Int
    private val enterAnimationId: Int

    private var animationEnabled: Boolean

    private lateinit var loadingOperation: (Boolean) -> Unit

    init {
        val inflater = LayoutInflater.from(c)

        val tArray = c.obtainStyledAttributes(a, R.styleable.StatefulLayout)

        contentViewId = tArray.getResourceId(R.styleable.StatefulLayout_contentView, 0)

        val errorResId = tArray.getResourceId(
                R.styleable.StatefulLayout_failureView,
                R.layout.state_failure_layout
        )
        inflater.inflate(errorResId, this)

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
                R.id.loadingStatusTextView
        )
        errorTextViewId = tArray.getResourceId(
                R.styleable.StatefulLayout_failureTextView,
                R.id.errorStatusTextView
        )
        emptyTextViewId = tArray.getResourceId(
                R.styleable.StatefulLayout_emptyTextView,
                R.id.emptyStatusTextView
        )

        //分配默认状态
        defaultStatus = tArray.getInt(R.styleable.StatefulLayout_state, 0)

        animationEnabled = tArray.getBoolean(R.styleable.StatefulLayout_animationEnabled, true)
        exitAnimationId = tArray.getResourceId(
                R.styleable.StatefulLayout_exitAnimation,
                R.anim.default_exit_state_layout
        )
        enterAnimationId = tArray.getResourceId(
                R.styleable.StatefulLayout_enterAnimation,
                R.anim.default_enter_state_layout
        )

        tArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        state = State.values().first {
            it.ordinal == defaultStatus
        }
    }

    fun setLoadingText(text: CharSequence?) {
        findViewById<TextView>(loadingTextViewId).text = text
    }

    fun setFailureText(text: CharSequence?) {
        findViewById<TextView>(errorTextViewId).text = text
    }

    fun setEmptyText(text: CharSequence?) {
        findViewById<TextView>(emptyTextViewId).text = text
    }

    fun setOnFailureTextClickListener(l: (View) -> Unit) {
        findViewById<TextView>(errorTextViewId).setOnClickListener(l)
    }

    fun setOnEmptyTextClickListener(l: (View) -> Unit) {
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
    }

    private fun View.show() {
        val visible = getTag(VISIBILITY)
        if (visible != "VISIBLE") {
            if (visible != null && animationEnabled) {
                startAnimation(AnimationUtils.loadAnimation(context, enterAnimationId))
            }
            setTag(VISIBILITY, "VISIBLE")
            visibility = VISIBLE
        }
    }

    private fun View.hide() {
        val visible = getTag(VISIBILITY)
        if (visible != "GONE") {
            if (visible != null && animationEnabled) {
                val animation = AnimationUtils.loadAnimation(context, exitAnimationId)
                animation.setAnimationListener(AnimationListener(this))
                startAnimation(animation)
            } else {
                setTag(VISIBILITY, "GONE")
                visibility = GONE
            }
        }
    }

    private class AnimationListener(private val view: View) : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) = Unit
        override fun onAnimationStart(animation: Animation?) = Unit
        override fun onAnimationEnd(animation: Animation?) {
            view.setTag(VISIBILITY, "GONE")
            view.visibility = GONE
        }
    }

    companion object {

        private const val VISIBILITY = 33554432

    }

}