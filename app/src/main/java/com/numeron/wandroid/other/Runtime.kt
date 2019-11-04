@file:JvmName("Runtime")

package com.numeron.wandroid.other

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import com.numeron.util.context


val database by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Room.databaseBuilder(context, Database::class.java, "shadow-demo.db")
            .build()
}


val preferences: SharedPreferences by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    context.getSharedPreferences("shadow-demo", Context.MODE_PRIVATE)
}


val connectivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}