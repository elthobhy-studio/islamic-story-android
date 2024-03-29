package com.elthobhy.islamicstory.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class ListEntity (

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "detail")
    val detail: String? = null,

    @ColumnInfo(name = "profile")
    val profile: String? = null,

    @ColumnInfo(name = "display")
    val display: String? = null,

    @ColumnInfo(name = "umur")
    val umur: String? = null,

    @ColumnInfo(name = "umat")
    val umat: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "key_id")
    val keyId: String,

    @ColumnInfo(name = "recentActivity")
    var recentActivity: Boolean = false,

    @ColumnInfo(name = "tag")
    var tag: String? = null,
)