package com.numeron.wandroid.activity

import androidx.appcompat.app.AppCompatActivity
import com.numeron.chameleon.Chameleon

abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        Chameleon.setTheme(this)
        super.setContentView(layoutResID)
    }

}