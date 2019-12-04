package com.numeron.result

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.*
import kotlin.collections.LinkedHashMap


private const val FRAGMENT_TAG = "EmptyFragment"

private val codeGenerator = Random()
private val resultHolder = LinkedHashMap<Int, LambdaHolder<Intent>>()
private val permissionHolder = LinkedHashMap<Int, LambdaHolder<Unit>>()


private fun <M : Map<Int, *>> codeGenerate(map: M): Int {
    var requestCode: Int
    do {
        requestCode = codeGenerator.nextInt(0xFFFF)
    } while (requestCode in map.keys)
    return requestCode
}


/**
 * 查找Activity中有没有EmptyFragment，如果没有则创建EmptyFragment并添加到Activity中
 * @receiver FragmentManager
 * @return EmptyFragment 已创建并已添加到Activity中的Fragment
 */
private fun findOrCreateEmptyFragment(manager: FragmentManager): EmptyFragment {
    return manager.findFragmentByTag(FRAGMENT_TAG) as? EmptyFragment ?: EmptyFragment().also {
        manager.beginTransaction().replace(android.R.id.content, it, FRAGMENT_TAG).commitNowAllowingStateLoss()
    }
}


/**
 * 启动Activity并接收Intent的扩展方法，接收回调时不需要重写[Activity#onActivityResult]方法
 * @receiver F  基于[FragmentActivity]的扩展方法
 * @param intent Intent [#startActivity]必需的参数
 * @param options Bundle?   动画参数
 * @param callback (data: Intent) -> Unit   返回此界面时，当[#resultCode]为 RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [LambdaHolder#onCanceled]或
 *      [LambdaHolder#onDefined] 方法来设置 ResultCode 为 RESULT_CANCELED 或 RESULT_FIRST_USER 时的回调
 */
fun <F : FragmentActivity> F.startActivityForResult(
        intent: Intent,
        options: Bundle? = null,
        callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    //获取一个与已有编码不重复的编码
    val requestCode = codeGenerate(resultHolder)
    //获取或创建Fragment
    val emptyFragment = findOrCreateEmptyFragment(supportFragmentManager)
    //调用Fragment的startActivityForResult方法，并传入回调
    return emptyFragment.startActivityForResult(requestCode, intent, options, callback)
}

fun <F : Fragment> F.startActivityForResult(
        intent: Intent,
        options: Bundle? = null,
        callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return requireActivity().startActivityForResult(intent, options, callback)
}


/**
 * 申请权限的扩展方法，通过lambda传入回调，不需要重写[Activity#onRequestPermissionsResult]方法
 * @receiver F  基于[FragmentActivity]的扩展方法
 * @param permission Array<out String>  要申请的权限
 * @param onRequestDone () -> Unit  申请成功时的回调
 * @return LambdaHolder<Unit>   可以在此对象上继续调用[#onDenied]方法来设置申请失败时的回调
 */
fun <F : FragmentActivity> F.requestPermissions(
        vararg permission: String,
        onRequestDone: () -> Unit): LambdaHolder<Unit> {
    //获取一个与已有编码不重复的编码
    val requestCode = codeGenerate(permissionHolder)
    //查找Activity中有没有空的Fragment，如果没有则创建空的Fragment并添加到Activity中
    val emptyFragment = findOrCreateEmptyFragment(supportFragmentManager)
    //使用Fragment的requestPermissions方法申请权限并传入回调
    return emptyFragment.requestPermissions(requestCode, *permission) {
        onRequestDone()
    }
}


/**
 * 启动Activity并接收Intent的扩展方法，不需要重写[#onActivityResult]方法
 * @receiver 基于[FragmentActivity]的扩展方法
 * @param params Array<out Pair<String, *>> 要携带的参数
 * @param options Bundle?   动画参数
 * @param callback (Intent) -> Unit 返回此界面时，当ResultCode为RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [LambdaHolder#onCanceled]或
 *          [LambdaHolder#onDefined]方法来设置ResultCode为RESULT_CANCELED或RESULT_FIRST_USER时的回调
 */
inline fun <reified F : FragmentActivity> FragmentActivity.startActivityForResult(
        vararg params: Pair<String, *>,
        options: Bundle? = null,
        noinline callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return startActivityForResult(Intent(this, F::class.java).putExtras(bundleOf(*params)), options, callback)
}


/**
 * 启动Activity并接收Intent的扩展方法，不需要重写[#onActivityResult]方法
 * @receiver 基于[Fragment]的扩展方法
 * @param params Array<out Pair<String, *>> 要携带的参数
 * @param options Bundle?   动画参数
 * @param callback (Intent) -> Unit 返回此界面时，当ResultCode为RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [#onCanceled]或[#onDefined]方法来
 *          设置ResultCode为RESULT_CANCELED或RESULT_FIRST_USER时的回调
 */
inline fun <reified F : FragmentActivity> Fragment.startActivityForResult(
        vararg params: Pair<String, *>,
        options: Bundle? = null,
        noinline callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return requireActivity().startActivityForResult(Intent(context, F::class.java).putExtras(bundleOf(*params)), options, callback)
}


internal class EmptyFragment : Fragment() {

    internal fun startActivityForResult(requestCode: Int, intent: Intent, options: Bundle? = null, callback: (Intent) -> Unit): LambdaHolder<Intent> {
        return LambdaHolder(callback).also {
            resultHolder[requestCode] = it
            startActivityForResult(intent, requestCode, options)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //取出与requestCode对应的对象，然后执行与resultCode对应的回调
        resultHolder.remove(requestCode)?.let {
            it.before()
            when (resultCode) {
                FragmentActivity.RESULT_OK -> it.onSuccess(data ?: Intent())
                FragmentActivity.RESULT_CANCELED -> it.onCanceled()
                else -> it.onDefined(data)
            }
        }
    }

    internal fun requestPermissions(requestCode: Int, vararg permissions: String, onRequestDone: (Unit) -> Unit): LambdaHolder<Unit> {
        return LambdaHolder(onRequestDone).also {
            //如果系统版本大于Android6.0并且未授予此权限，则申请权限
            if (Build.VERSION.SDK_INT > 22 && !checkPermissions(*permissions)) {
                //将回调加入待调用Map存起来，然后申请权限
                permissionHolder[requestCode] = it
                requestPermissions(permissions, requestCode)
            } else {
                //否则当作申请成功处理
                it.onSuccess(Unit)
            }
        }
    }

    private fun checkPermissions(vararg permission: String): Boolean {
        return permission.all {
            ActivityCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //取出与requestCode对应的回执记录，如果为空，则结束此方法
        val lambdaHolder = permissionHolder.remove(requestCode) ?: return
        //当有正在申请的权限未结束时，permissions和grantResults会是空的，此时为申请失败，做中断处理
        if (permissions.isEmpty() && grantResults.isEmpty()) return
        //将未授予的权限加入到一个列表中
        grantResults.toList().mapIndexedNotNull { index, result ->
            if (result != PackageManager.PERMISSION_GRANTED) permissions[index] else null
        }.let {
            //通过列表是否为空来判断权限是否授予，然后执行对应的回调
            if (it.isEmpty()) lambdaHolder.onSuccess(Unit) else lambdaHolder.onDenied(it)
        }
    }

}