package com.elthobhy.islamicstory.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult

class RegisterViewModel(private val useCase: UseCase) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<Resource<AuthResult>> =
        useCase.getDataRegister(name, email, password)
}