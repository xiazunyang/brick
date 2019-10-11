package com.numeron.wan.util

import android.app.Application
import com.numeron.brick.installRetrofit

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        installRetrofit(Http)
    }

}