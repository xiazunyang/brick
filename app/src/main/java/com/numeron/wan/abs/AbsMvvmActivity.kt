@file:Suppress("DEPRECATION")

package com.numeron.wan.abs

import android.app.ProgressDialog
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.numeron.brick.IView
import com.numeron.wan.util.DialogButton
import com.numeron.wan.util.NegativeButton
import com.numeron.wan.util.PositiveButton

abstract class AbstractMVVMActivity : AppCompatActivity(), IView {

    private lateinit var progressDialog: ProgressDialog

    override fun showLoading(message: String, isCancelable: Boolean) {
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