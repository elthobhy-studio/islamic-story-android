package com.aljebrastudio.islamicstory.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential

class ChangePasswordViewModel(private val useCase: UseCase): ViewModel() {
    fun changePassword(newPass: String, credential: AuthCredential): LiveData<Resource<Void>> =
        useCase.changePassword(newPass, credential)
}