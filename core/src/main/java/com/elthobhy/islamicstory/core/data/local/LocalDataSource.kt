package com.elthobhy.islamicstory.core.data.local

import com.elthobhy.islamicstory.core.data.local.entity.ListEntity
import com.elthobhy.islamicstory.core.data.local.room.LocalDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val dao: LocalDao) {
    fun getList(): Flow<List<ListEntity>> = dao.getList()
    suspend fun insert(entity: List<ListEntity>) = dao.insert(entity)
    suspend fun delete(keyId: String) = dao.delete(keyId)
    fun getSearch(search: String): Flow<List<ListEntity>> = dao.getSearch(search)
    fun getRecentActivity(): Flow<List<ListEntity>> = dao.getRecentActivity()
    fun setRecentActivity(story: ListEntity, state: Boolean){
        story.recentActivity = state
        dao.updateData(story)
    }
    fun updateRecentActivity(state: Boolean, story: ListEntity){
        story.recentActivity = state
        dao.updateData(story)
    }
}