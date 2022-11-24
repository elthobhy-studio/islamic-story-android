package com.aljebrastudio.islamicstory.core.data

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.data.remote.RemoteDataSource
import com.aljebrastudio.islamicstory.core.domain.repository.RepositoryInterface
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

class Repository(
    private val remoteDataSource: RemoteDataSource
): RepositoryInterface {
    override fun getDataRegister(email: String, password: String): LiveData<Resource<AuthResult>> {
        return remoteDataSource.register(email, password)
    }

    override fun getDataLogin(email: String, password: String): LiveData<Resource<AuthResult>> {
        return remoteDataSource.login(email, password)
    }
}