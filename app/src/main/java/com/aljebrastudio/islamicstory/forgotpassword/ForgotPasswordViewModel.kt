package com.aljebrastudio.islamicstory.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.core.utils.vo.Resource

class ForgotPasswordViewModel(private val userCase: UseCase): ViewModel() {
    fun forgotPassword(email: String): LiveData<Resource<Void>> = userCase.forgotPassword(email)
}