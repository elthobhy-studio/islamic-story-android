package com.elthobhy.islamicstory.core.data.local.room

import androidx.room.*
import com.elthobhy.islamicstory.core.data.local.entity.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {
    @Query("SELECT * FROM story")
    fun getList(): Flow<List<ListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<ListEntity>)

    @Query("DELETE FROM story WHERE key_id = :keyId")
    suspend fun delete(keyId : String)

    @Update
    fun updateData(entity: ListEntity)
}