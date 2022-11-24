package com.aljebrastudio.islamicstory.core.domain.usecase

import androidx.lifecycle.LiveData
import com.aljebrastudio.islamicstory.core.domain.repository.RepositoryInterface
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult

class RepositoryInteract(private val repositoryInterface: RepositoryInterface): UseCase {
    override fun getDataRegister(email: String, password: String): LiveData<Resource<AuthResult>> = repositoryInterface.getDataRegister(email, password)
}