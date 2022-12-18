package com.elthobhy.islamicstory.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elthobhy.islamicstory.core.data.local.entity.ListEntity

@Database(entities = [ListEntity::class], version = 1, exportSchema = false)
abstract class DatabaseLocal : RoomDatabase() {
    abstract fun dao(): LocalDao
}