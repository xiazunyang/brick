package com.numeron.wandroid.other

import android.content.pm.ApplicationInfo
import android.content.res.Resources


private lateinit var sPluginLoadCallback: LoadPluginCallback


val loadPluginCallback
    get() = sPluginLoadCallback


fun setLoadPluginCallback(callback: LoadPluginCallback) {
    sPluginLoadCallback = callback
}


interface LoadPluginCallback {

    fun beforeLoadPlugin(partKey: String)

    fun afterLoadPlugin(partKey: String, applicationInfo: ApplicationInfo, pluginClassLoader: ClassLoader, pluginResources: Resources)

}