package com.elthobhy.islamicstory.core.data

import androidx.lifecycle.LiveData
import com.elthobhy.islamicstory.core.data.remote.RemoteDataSource
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.model.User
import com.elthobhy.islamicstory.core.domain.repository.RepositoryInterface
import com.elthobhy.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import java.io.File

class Repository(
    private val remoteDataSource: RemoteDataSource,
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

    override fun getList(): LiveData<Resource<List<ListDomain>>> {
        return remoteDataSource.getData()
    }

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

    override fun removeData(keyId: String): LiveData<Resource<String>> {
        return remoteDataSource.removeData(keyId)
    }
}