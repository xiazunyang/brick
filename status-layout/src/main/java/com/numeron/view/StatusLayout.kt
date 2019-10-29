package com.numeron.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import com.numeron.common.Status

class StatusLayout @JvmOverloads constructor(c: Context, a: AttributeSet? = null, i: Int = 0) :
        FrameLayout(c, a, i) {

    private var previousStatus: Status? = null
    private var hookLoading = false

    /**
     * 设置状态，设置空时，将恢复上一个状态
     */
    var status: Status? = null
        //当设置状态时，将状态压入栈中
        set(value) {
            if (value == null && previousStatus != null) {
                field = previousStatus
                previousStatus = null
                changeView()
            } else if (value != field) {
                previousStatus = field
                field = value
                changeView()
            }
        }

    private val errorView
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

        val tArray = c.obtainStyledAttributes(a, R.styleable.StatusLayout)

        contentViewId = tArray.getResourceId(R.styleable.StatusLayout_contentView, 0)

        val errorResId = tArray.getResourceId(
                R.styleable.StatusLayout_errorView,
                R.layout.status_error_layout
        )
        inflater.inflate(errorResId, this)

        val emptyResId = tArray.getResourceId(
                R.styleable.StatusLayout_emptyView,
                R.layout.status_empty_layout
        )
        inflater.inflate(emptyResId, this)

        val loadingResId = tArray.getResourceId(
                R.styleable.StatusLayout_loadingView,
                R.layout.status_loading_layout
        )
        inflater.inflate(loadingResId, this)

        loadingTextViewId = tArray.getResourceId(
                R.styleable.StatusLayout_loadingTextView,
                R.id.loadingStatusTextView
        )
        errorTextViewId = tArray.getResourceId(
                R.styleable.StatusLayout_errorTextView,
                R.id.errorStatusTextView
        )
        emptyTextViewId = tArray.getResourceId(
                R.styleable.StatusLayout_emptyTextView,
                R.id.emptyStatusTextView
        )

        //分配默认状态
        defaultStatus = tArray.getInt(R.styleable.StatusLayout_status, 0)

        animationEnabled = tArray.getBoolean(R.styleable.StatusLayout_animationEnabled, true)
        exitAnimationId = tArray.getResourceId(
                R.styleable.StatusLayout_exitAnimation,
                R.anim.default_exit_status_layout
        )
        enterAnimationId = tArray.getResourceId(
                R.styleable.StatusLayout_enterAnimation,
                R.anim.default_enter_status_layout
        )

        tArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        status = Status.values().first {
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

    fun recoveryStatus() {
        status = previousStatus
    }

    private fun changeView() {
        if (::loadingOperation.isInitialized) {
            loadingOperation(status == Status.Loading)
        }
        if (!hookLoading && status == Status.Loading) loadingView?.show() else loadingView?.hide()
        if (status == Status.Success) contentView?.show() else contentView?.hide()
        if (status == Status.Failure) errorView?.show() else errorView?.hide()
        if (status == Status.Empty) emptyView?.show() else emptyView?.hide()
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