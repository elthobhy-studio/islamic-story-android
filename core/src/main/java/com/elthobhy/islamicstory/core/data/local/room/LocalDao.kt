package com.elthobhy.islamicstory.core.data.local.room

import androidx.room.*
import com.elthobhy.islamicstory.core.data.local.entity.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {
    @Query("SELECT * FROM story WHERE tag = :nabi")
    fun getList(nabi: String): Flow<List<ListEntity>>

    @Query("SELECT * FROM story WHERE tag = :shirah")
    fun getListShirah(shirah: String): Flow<List<ListEntity>>

    @Query("SELECT * FROM story WHERE tag = :khalifah")
    fun getListKhalifah(khalifah: String): Flow<List<ListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<ListEntity>)

    @Query("DELETE FROM story WHERE key_id = :keyId")
    suspend fun delete(keyId : String)

    @Query("SELECT * FROM story WHERE name LIKE '%' || :search || '%' OR tag = :tag")
    fun getSearch(search: String, tag: String?): Flow<List<ListEntity>>

    @Query("SELECT * FROM story WHERE recentActivity = 1")
    fun getRecentActivity(): Flow<List<ListEntity>>

    @Update
    fun updateData(entity: ListEntity)
}