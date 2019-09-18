package com.numeron.wan.abs

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.numeron.frame.base.IModel
import com.numeron.frame.mvvm.AbstractViewModel
import com.numeron.frame.mvvm.IView
import com.numeron.frame.mvvm.createViewModel
import com.numeron.wan.util.DialogButton
import com.numeron.wan.util.Http
import com.numeron.wan.util.NegativeButton
import com.numeron.wan.util.PositiveButton
import kotlinx.coroutines.cancel

/**
 * 实现自己的抽象View，需要实现IView中所有的方法
 * 以及在onDestroy方法中调用viewModel.cancel()，用于在此View的生命周期结束时，终止所有协程的运行
 */
abstract class AbsMvvmActivity<out VM : AbstractViewModel<IView<VM>, IModel>> : AppCompatActivity(), IView<VM> {

    private lateinit var progressDialog: ProgressDialog
    override lateinit var viewModel: @UnsafeVariance VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ViewModel必需在生命周期生效后才能创建
        viewModel = createViewModel(Http)
    }

    override fun showLoading(message: String?, title: String, isCancelable: Boolean) {
        if (::progressDialog.isInitialized) {
            //如果已初始化，但是未显示，则设置数据后，重新显示
            if (!progressDialog.isShowing) {
                progressDialog.setTitle(title)
                progressDialog.setMessage(message)
                progressDialog.setCancelable(isCancelable)
                progressDialog.show()
            }
        } else {
            //如果没有初始化，则创建一个
            progressDialog = ProgressDialog.show(
                    this, title, message, false, isCancelable)
        }
    }

    override fun hideLoading() {
        //如果已初始化并且正在显示，则关闭等待框
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onDestroy() {
        viewModel.cancel()
        super.onDestroy()
    }

    /* 建议此方法也写入IView接口中，毕竟Activity和Fragment中经常需要弹出对话框，根据业务需求决定 */
    fun showDialog(message: String, title: String = "提示", vararg buttons: DialogButton) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .apply {
                    buttons.forEach {
                        when (it) {
                            is PositiveButton -> setPositiveButton(it.text) { _, _ ->
                                it.onClick()
                            }
                            is NegativeButton -> setNegativeButton(it.text) { _, _ ->
                                it.onClick()
                            }
                            else -> setNeutralButton(it.text) { _, _ ->
                                it.onClick()
                            }
                        }
                    }
                }
                .show()
    }

}