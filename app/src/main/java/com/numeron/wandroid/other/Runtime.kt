@file:JvmName("Runtime")

package com.numeron.wandroid.other

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.numeron.util.application
import com.numeron.util.context


val database by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Room.databaseBuilder(context, Database::class.java, "shadow-demo.db")
            .build()
}


val preferences: SharedPreferences by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    context.getSharedPreferences("shadow-demo", Context.MODE_PRIVATE)
}


val viewModelFactory by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
}


val connectivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}