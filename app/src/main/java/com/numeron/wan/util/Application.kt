package com.numeron.wan.util

import android.app.Application
import com.numeon.brick.ModelFactory

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        ModelFactory.install(Http)
    }

}