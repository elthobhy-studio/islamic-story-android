package com.aljebrastudio.islamicstory.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

class LoginViewModel(private val useCase: UseCase) : ViewModel() {
    fun login(email: String, password: String): LiveData<Resource<AuthResult>> =
        useCase.getDataLogin(email, password)

    fun loginWithGoogle(
        name: String,
        email: String,
        credential: AuthCredential
    ): LiveData<Resource<AuthResult>> =
        useCase.loginWithGoogle(name, email, credential)
}