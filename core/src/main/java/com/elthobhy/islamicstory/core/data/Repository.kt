package com.elthobhy.islamicstory.core.data

import androidx.lifecycle.LiveData
import com.elthobhy.islamicstory.core.data.local.LocalDataSource
import com.elthobhy.islamicstory.core.data.local.entity.ListEntity
import com.elthobhy.islamicstory.core.data.remote.RemoteDataSource
import com.elthobhy.islamicstory.core.data.remote.response.ListResponseItem
import com.elthobhy.islamicstory.core.data.remote.vo.ApiResponse
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.model.User
import com.elthobhy.islamicstory.core.domain.repository.RepositoryInterface
import com.elthobhy.islamicstory.core.utils.DataMapper
import com.elthobhy.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
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

    override fun getList(): Flow<Resource<List<ListDomain>>> =
        object : NetworkBoundResource<List<ListDomain>, List<ListResponseItem>>(){
            override suspend fun loadFromDb(): Flow<List<ListDomain>> {
                return localDataSource.getList().map { DataMapper.mapEntityToDomain(it) }
            }

            override fun shouldFetch(data: List<ListDomain>?): Boolean {
                return true
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
        nama: String,
        umur: String,
        tempatDiutus: String,
        kisah: String,
        keyId: String,
        profile: File,
        display: File,
    ): LiveData<Resource<ListDomain>> {
        return remoteDataSource.postDataNabi(nama, umur, tempatDiutus, kisah, keyId,profile, display)
    }

    override suspend fun removeData(keyId: String): LiveData<Resource<String>> {
        localDataSource.delete(keyId)
        return remoteDataSource.removeData(keyId)
    }

    override fun getSearch(search: String): Flow<List<ListDomain>> {
        return localDataSource.getSearch(search).map{
            DataMapper.mapEntityToDomain(it)
        }
    }
}