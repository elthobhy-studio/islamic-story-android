package com.aljebrastudio.islamicstory.core.data

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.data.remote.RemoteDataSource
import com.aljebrastudio.islamicstory.core.domain.model.User
import com.aljebrastudio.islamicstory.core.domain.repository.RepositoryInterface
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

class Repository(
    private val remoteDataSource: RemoteDataSource
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
}