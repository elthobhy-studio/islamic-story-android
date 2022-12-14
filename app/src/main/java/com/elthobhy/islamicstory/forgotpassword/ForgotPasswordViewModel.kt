package com.elthobhy.islamicstory.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.core.utils.vo.Resource

class ForgotPasswordViewModel(private val userCase: UseCase): ViewModel() {
    fun forgotPassword(email: String): LiveData<Resource<Void>> = userCase.forgotPassword(email)
}