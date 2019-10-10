package com.numeron.wan.util

import android.app.Application
import com.numeron.brick.ModelFactory

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        ModelFactory.install(Http)
    }

}