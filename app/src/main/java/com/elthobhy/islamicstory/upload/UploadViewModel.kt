package com.elthobhy.islamicstory.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.core.utils.vo.Resource
import java.io.File

class UploadViewModel(private val userCase: UseCase) : ViewModel() {
    fun postDataNabi(
        nama: String,
        umur: String,
        tempatDiutus: String,
        kisah: String,
        keyId: String,
        profile: File,
        display: File,
        ): LiveData<Resource<ListDomain>> = userCase.postDataNabi(nama, umur, tempatDiutus, kisah, keyId, profile, display)
    suspend fun removeData(keyId: String?): LiveData<Resource<String>>? = keyId?.let { userCase.removeData(it) }

}