package com.aljebrastudio.islamicstory.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult

class RegisterViewModel(private val useCase: UseCase): ViewModel() {
    fun register(email: String, password: String): LiveData<Resource<AuthResult>> = useCase.getDataRegister(email, password)
}