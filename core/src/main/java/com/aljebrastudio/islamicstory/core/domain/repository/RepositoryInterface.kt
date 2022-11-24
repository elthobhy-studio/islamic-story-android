package com.aljebrastudio.islamicstory.core.domain.repository

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    fun getDataRegister(email: String, password: String): LiveData<Resource<AuthResult>>
}