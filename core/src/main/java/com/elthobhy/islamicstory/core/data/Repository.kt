package com.elthobhy.islamicstory.core.data

import androidx.lifecycle.LiveData
import com.elthobhy.islamicstory.core.data.local.LocalDataSource
import com.elthobhy.islamicstory.core.data.remote.RemoteDataSource
import com.elthobhy.islamicstory.core.data.remote.response.ListResponseItem
import com.elthobhy.islamicstory.core.data.remote.vo.ApiResponse
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.model.User
import com.elthobhy.islamicstory.core.domain.repository.RepositoryInterface
import com.elthobhy.islamicstory.core.utils.AppExecutors
import com.elthobhy.islamicstory.core.utils.Constants
import com.elthobhy.islamicstory.core.utils.DataMapper
import com.elthobhy.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): RepositoryInterface {
    override fun getDataRegister(name: String, email: String, password: String): LiveData<Resource<AuthResult>> {
        return remoteDataSource.register(name, email, password)
    }

    override fun getDataLogin(email: String, password: String): LiveData<Resource<AuthResult>> {
        return remoteDataSource.login(email, password)
    }

    override fun getDataUser(uid: String): LiveData<Resource<User>> {
        return remoteDataSource.getDataUser(uid)
    }

    override fun loginWithGoogle(
        name: String,
        email: String,
        credential: AuthCredential
    ): LiveData<Resource<AuthResult>> {
        return remoteDataSource.loginWithGoogle(name, email, credential)
    }

    override fun changePassword(
        newPass: String,
        credential: AuthCredential
    ): LiveData<Resource<Void>> {
        return remoteDataSource.changePassword(newPass, credential)
    }

    override fun forgotPassword(email: String): LiveData<Resource<Void>> {
        return remoteDataSource.forgotPassword(email)
    }

    override fun getList(tag: String): Flow<Resource<List<ListDomain>>> =
        object : NetworkBoundResource<List<ListDomain>, List<ListResponseItem>>(){
            override suspend fun loadFromDb(): Flow<List<ListDomain>> {
                return when(tag){
                    Constants.NABI -> {
                        localDataSource.getListNabi(tag).map { DataMapper.mapEntityToDomain(it) }
                    }
                    Constants.SHIRAH -> {
                        localDataSource.getListShirah(tag).map { DataMapper.mapEntityToDomain(it) }
                    }
                    else -> {localDataSource.getListNabi(tag).map { DataMapper.mapEntityToDomain(it) }}
                }
            }

            override fun shouldFetch(
                data: List<ListDomain>?,
                dataNew: List<ListResponseItem>?
            ): Boolean {
                return data != dataNew || data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ListResponseItem>>> {
                return remoteDataSource.getList()
            }

            override suspend fun saveCallResult(data: List<ListResponseItem>) {
                val dataMap = DataMapper.mapResponseToEntity(data)
                return localDataSource.insert(dataMap)
            }
        }.asFlow()

    override fun postDataNabi(
        nama: String?,
        umur: String?,
        tempatDiutus: String?,
        kisah: String?,
        keyId: String,
        profile: File?,
        display: File?,
        recentActivity: Boolean,
        tag: String
    ): LiveData<Resource<ListDomain>> {
        return remoteDataSource.postDataNabi(nama, umur, tempatDiutus, kisah, keyId,profile, display,recentActivity, tag)
    }

    override suspend fun removeData(keyId: String): LiveData<Resource<String>> {
        localDataSource.delete(keyId)
        return remoteDataSource.removeData(keyId)
    }

    override fun getSearch(search: String, tag: String?): Flow<List<ListDomain>> {
        return localDataSource.getSearch(search, tag).map{
            DataMapper.mapEntityToDomain(it)
        }
    }

    override fun getRecentActivity(): Flow<List<ListDomain>> {
        return localDataSource.getRecentActivity().map {
            DataMapper.mapEntityToDomain(it)
        }
    }

    override fun setRecentActivity(story: ListDomain, state: Boolean, keyId: String) {
        val entity = DataMapper.mapDomainToEntity(story)
        remoteDataSource.setRecentActivity(state, keyId)
        return appExecutors.diskIO().execute{
            if (entity != null) {
                localDataSource.setRecentActivity(entity, state)
            }
        }
    }

    override fun clearRecentActivity(state: Boolean, keyId: String, story: ListDomain) {
        val newStory = DataMapper.mapDomainToEntity(story)
        remoteDataSource.clearRecentActivity(state, keyId)
        return appExecutors.diskIO().execute{
            if (newStory != null) {
                localDataSource.updateRecentActivity(state,newStory)
            }
        }

    }
}