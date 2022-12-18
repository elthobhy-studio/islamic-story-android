package com.elthobhy.islamicstory.core.domain.usecase

import androidx.lifecycle.LiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.model.User
import com.elthobhy.islamicstory.core.domain.repository.RepositoryInterface
import com.elthobhy.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import java.io.File

class RepositoryInteract(private val repositoryInterface: RepositoryInterface) : UseCase {
    override fun getDataRegister(name: String, email: String, password: String): LiveData<Resource<AuthResult>> =
        repositoryInterface.getDataRegister(name, email, password)

    override fun getDataLogin(email: String, password: String): LiveData<Resource<AuthResult>> =
        repositoryInterface.getDataLogin(email, password)

    override fun getDataUser(uid: String): LiveData<Resource<User>> =
        repositoryInterface.getDataUser(uid)

    override fun loginWithGoogle(
        name: String,
        email: String,
        credential: AuthCredential
    ): LiveData<Resource<AuthResult>> =
        repositoryInterface.loginWithGoogle(name,email,credential)

    override fun changePassword(
        newPass: String,
        credential: AuthCredential
    ): LiveData<Resource<Void>> = repositoryInterface.changePassword(newPass, credential)

    override fun forgotPassword(email: String): LiveData<Resource<Void>> =
        repositoryInterface.forgotPassword(email)

    override fun getData(): Flow<Resource<List<ListDomain>>> {
        return repositoryInterface.getList()
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
        return repositoryInterface.postDataNabi(nama, umur, tempatDiutus, kisah, keyId,profile, display)
    }

    override suspend fun removeData(keyId: String): LiveData<Resource<String>> {
        return repositoryInterface.removeData(keyId)
    }

}