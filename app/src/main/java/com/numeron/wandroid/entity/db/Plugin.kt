package com.numeron.wandroid.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.numeron.common.Identifiable

@Entity
data class Plugin(

        @SerializedName("lid")
        @PrimaryKey(autoGenerate = true)
        override val id: Long,

        val userId: Long,

        val code: String,

        val hasPermission: Boolean,

        val visible: Boolean,

        val sort: Int,

        val download: String?,

        @SerializedName("package")
        val identity: String?,

        val boot: String?,

        val name: String?,

        val iconUrl: String?

) : Identifiable<Long>