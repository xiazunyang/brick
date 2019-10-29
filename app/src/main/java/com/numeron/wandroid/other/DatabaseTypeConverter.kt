package com.numeron.wandroid.other

import androidx.room.TypeConverter
import java.util.*

class DatabaseTypeConverter {

    @TypeConverter
    fun Long.toDate(): Date? {
        if (this <= 0L) return null
        return Date(this)
    }

    @TypeConverter
    fun Date?.toLong(): Long {
        if (this == null) return 0
        return time
    }

}

