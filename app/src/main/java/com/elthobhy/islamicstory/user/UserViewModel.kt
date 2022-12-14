package com.elthobhy.islamicstory.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elthobhy.islamicstory.core.domain.model.User
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.core.utils.vo.Resource

class UserViewModel(private val userCase: UseCase) : ViewModel() {
    fun getDataUser(uid: String): LiveData<Resource<User>> = userCase.getDataUser(uid)
}