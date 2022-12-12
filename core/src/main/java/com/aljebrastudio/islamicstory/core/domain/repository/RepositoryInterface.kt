package com.aljebrastudio.islamicstory.core.domain.repository

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain
import com.aljebrastudio.islamicstory.core.domain.model.User
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface RepositoryInterface {
    fun getDataRegister(name: String, email: String, password: String): LiveData<Resource<AuthResult>>
    fun getDataLogin(email: String, password: String): LiveData<Resource<AuthResult>>
    fun getDataUser(uid: String): LiveData<Resource<User>>
    fun loginWithGoogle(name: String, email: String, credential: AuthCredential): LiveData<Resource<AuthResult>>
    fun changePassword(newPass: String, credential: AuthCredential): LiveData<Resource<Void>>
    fun forgotPassword(email: String): LiveData<Resource<Void>>
    fun getList(): LiveData<List<ListDomain>>
    fun postDataNabi(
        nama: String,
        umur: String,
        tempatDiutus: String,
        kisah: String,
        keyId: String
    ): LiveData<Resource<ListDomain>>
}