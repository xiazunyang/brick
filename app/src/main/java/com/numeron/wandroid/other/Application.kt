package com.numeron.wandroid.other

import android.app.Application
import com.numeron.brick.install
import com.numeron.util.initContext

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initContext(this)
        install(Http.retrofit, database)
    }

}