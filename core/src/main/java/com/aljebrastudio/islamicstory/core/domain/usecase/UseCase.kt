package com.aljebrastudio.islamicstory.core.domain.usecase

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult

interface UseCase {
    fun getDataRegister(email: String, password: String): LiveData<Resource<AuthResult>>
}