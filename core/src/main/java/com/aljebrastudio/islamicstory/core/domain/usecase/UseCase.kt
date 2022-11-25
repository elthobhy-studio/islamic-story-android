package com.aljebrastudio.islamicstory.core.domain.usecase

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.domain.model.User
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface UseCase {
    fun getDataRegister(name: String, email: String, password: String): LiveData<Resource<AuthResult>>
    fun getDataLogin(email: String, password: String): LiveData<Resource<AuthResult>>
    fun getDataUser(uid: String): LiveData<Resource<User>>
    fun loginWithGoogle(name: String, email: String, credential: AuthCredential): LiveData<Resource<AuthResult>>
    fun changePassword(newPass: String, credential: AuthCredential): LiveData<Resource<Void>>
}