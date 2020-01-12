package com.numeron.chameleon

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.SparseArray
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

typealias ColorProcessor = (View, Int) -> Unit

/**
 * 应用当前主题颜色，使用此工具修改主题可立即生效，无需重新创建Activity，
 * 也不会出现闪屏的现象，非常轻量级，侵入性和接入成本极低，推荐没有加载外置皮肤需求的项目使用！
 * @property themeId Int 主题ID，应该是R.style.xxx这样的格式
 */
open class Chameleon(context: Context, private val themeId: Int) {

    private val theme = context.resources.newTheme()

    init {
        theme.applyStyle(themeId, true)
    }

    private val textColors = SparseIntArray()
    private val tintColors = SparseIntArray()
    private val backgroundColors = SparseIntArray()
    private val backgroundTintColors = SparseIntArray()
    private val reflectionColors = SparseArray<Pair<String, Int>>()
    private val processorColors = SparseArray<Pair<Int, ColorProcessor>>()

    private fun getColor(attrId: Int): Int {
        val value = TypedValue()
        theme.resolveAttribute(attrId, value, true)
        return value.data
    }

    /**
     * 使用着色器修改ImageView的显示颜色
     * @param imageViewId Int 要修改颜色的ImageView的Id
     * @param attrId Int    要修改的颜色的attr属性
     * @return Chameleon
     */
    fun setTintColor(imageViewId: Int, attrId: Int): Chameleon {
        tintColors.put(imageViewId, getColor(attrId))
        return this
    }

    /**
     * 设置TextView的字体颜色
     * @param textViewId Int 要修改颜色的TextView的Id
     * @param attrId Int    要修改的颜色的attr属性
     */
    fun setTextColor(textViewId: Int, attrId: Int): Chameleon {
        textColors.put(textViewId, getColor(attrId))
        return this
    }

    /**
     * 设置View的background的颜色
     * @param viewId Int 要修改背景颜色的View的Id
     * @param attrId Int 颜色的attr属性
     */
    fun setBackgroundColor(viewId: Int, attrId: Int): Chameleon {
        backgroundColors.put(viewId, getColor(attrId))
        return this
    }

    /**
     * 使用着色器修改View的background的颜色
     * @param viewId Int 要修改背景颜色的View的Id
     * @param attrId Int 颜色的attr属性
     */
    fun setBackgroundTintColor(viewId: Int, attrId: Int): Chameleon {
        backgroundTintColors.put(viewId, getColor(attrId))
        return this
    }

    /**
     * 使用反射修改View的颜色，用于其它方法无法满足需求时使用，比如要修改CardView的背景颜色。
     * @param viewId Int 要修改颜色的View的Id
     * @param methodName String 方法名称，区分大小写
     * @param attrId Int 颜色的attr属性
     */
    fun setColorByReflection(viewId: Int, methodName: String, attrId: Int): Chameleon {
        reflectionColors.put(viewId, methodName to getColor(attrId))
        return this
    }

    /**
     * 当某些View修改颜色的方法接收的不是Int类型时，可用此方法进行转换。
     * @param viewId Int 要修改颜色的View的Id
     * @param attrId Int 颜色的attr属性
     * @param processor Function2<View, Int, Unit> 自定义的颜色处理器
     */
    fun setColorByProcessor(viewId: Int, attrId: Int, processor: ColorProcessor): Chameleon {
        processorColors.put(viewId, getColor(attrId) to processor)
        return this
    }

    /**
     * 仅在无法获取到Activity时，才使用此方法来应用修改。
     * @param view View 传入当前界面中任意的一个View对象，不能是Dialog中的View。
     */
    fun apply(view: View) {
        view.context.setTheme(themeId)
        view.context.currentThemeId = themeId
        eachView(view.rootView)
    }

    /**
     * 调用此方法从Activity的根视图开始遍历View Tree，在此过程中根据 View Id来动态修改View的颜色。
     * @param activity Activity 要修改主题颜色的Activity
     * @param statusBarColor Int? 如果要同时修改状态栏的颜色，则需要传入一个非null的值
     */
    @JvmOverloads
    fun apply(activity: Activity, statusBarColor: Int? = null) {
        val window = activity.window
        if (statusBarColor != null) {
            window.statusBarColor = getColor(statusBarColor)
        }
        apply(window.decorView)
    }

    private fun nonzero(int: Int) = int != 0

    private fun eachView(view: View) {
        val viewId = view.id
        //用着色器修改ImageView的颜色
        tintColors[viewId].takeIf(::nonzero)?.let {
            (view as ImageView).imageTintList = ColorStateList.valueOf(it)
        }
        //修改TextView的字体颜色
        textColors[viewId].takeIf(::nonzero)?.let((view as TextView)::setTextColor)
        //修改View的背景颜色
        backgroundColors[viewId].takeIf(::nonzero)?.let(view::setBackgroundColor)
        //使用着色器修改背景颜色
        backgroundTintColors[viewId].takeIf(::nonzero)?.let {
            view.backgroundTintList = ColorStateList.valueOf(it)
        }
        //用反射修改以上方法无法满足的颜色
        reflectionColors[viewId]?.let { (methodName, color) ->
            view.javaClass.getMethod(methodName, Int::class.java).invoke(view, color)
        }
        //自定义处理器，用于非颜色参数的修改，比如ColorStateList
        processorColors[viewId]?.let { (color, processor) ->
            processor(view, color)
        }
        //处理RecyclerView中未显示的缓存视图
        if (view is RecyclerView) {
            recyclerViewProcess(view)
        }
        //遍历子View
        if (view is ViewGroup) {
            repeat(view.childCount) {
                eachView(view.getChildAt(it))
            }
        }
    }

    private fun recyclerViewProcess(recyclerView: RecyclerView) {
        //从回收器中修改
        val recycler = getRecycler(recyclerView)
        eachCaches(recycler, "mCachedViews")
        eachCaches(recycler, "mChangedScrap")
        eachCaches(recycler, "mAttachedScrap")
        //从循环池中修改
        eachPool(recyclerView.recycledViewPool)
    }

    private fun getRecycler(recyclerView: RecyclerView): RecyclerView.Recycler {
        val recyclerField = RecyclerView::class.java.getDeclaredField("mRecycler")
        recyclerField.isAccessible = true
        return recyclerField.get(recyclerView) as RecyclerView.Recycler
    }

    private fun eachPool(pool: RecyclerView.RecycledViewPool) {
        val field = RecyclerView.RecycledViewPool::class.java.getDeclaredField("mScrap")
        field.isAccessible = true
        val scrap = field.get(pool) ?: return
        if (scrap is SparseArray<*>) {
            scrap.valueIterator().forEach { scrapData ->
                val scrapHeap = getScrapHeap(scrapData ?: return@forEach) ?: return@forEach
                scrapHeap.forEach { viewHolder ->
                    if (viewHolder is RecyclerView.ViewHolder) {
                        eachView(viewHolder.itemView)
                    }
                }
            }
        }
    }

    private fun eachCaches(recycler: RecyclerView.Recycler, fieldName: String) {
        val field = recycler.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        val list = field.get(recycler) as? List<*> ?: return
        list.forEach {
            if (it is RecyclerView.ViewHolder) {
                eachView(it.itemView)
            }
        }
    }

    private fun getScrapHeap(any: Any): List<*>? {
        val scrapDataClass = Class.forName("androidx.recyclerview.widget.RecyclerView\$RecycledViewPool\$ScrapData")
        val scrapHeapField = scrapDataClass.getDeclaredField("mScrapHeap")
        scrapHeapField.isAccessible = true
        return scrapHeapField.get(any) as? List<*>
    }

    companion object {

        private const val SHARED_PREFERENCES_NAME = "chameleon"
        private const val SHARED_PREFERENCES_KEY = "current_theme_id"

        /**
         * 在项目的Activity基类中重写setContent方法，并在调用super方法之前，调用此静态方法。
         * @param activity Activity Activity基类
         */
        @JvmStatic
        fun setTheme(activity: Activity) {
            activity.setTheme(activity.currentThemeId)
        }

        @JvmStatic
        fun getThemeId(context: Context): Int {
            return context.currentThemeId
        }

        private var Context.currentThemeId: Int
            get() = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .getInt(SHARED_PREFERENCES_KEY, 0)
            set(value) {
                getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putInt(SHARED_PREFERENCES_KEY, value)
                        .apply()
            }

        private fun <T> SparseArray<T>.valueIterator(): Iterator<T> = object : Iterator<T> {
            var index = 0
            override fun hasNext() = index < size()
            override fun next() = valueAt(index++)
        }

    }

}