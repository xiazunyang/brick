package com.numeron.wandroid.other

import androidx.room.TypeConverter
import java.util.*

class DatabaseTypeConverter {

    @TypeConverter
    fun Long?.toDate(): Date? {
        if (this == null) return null
        return Date(this)
    }

    @TypeConverter
    fun Date?.toLong(): Long? {
        if (this == null) return null
        return time
    }

}

