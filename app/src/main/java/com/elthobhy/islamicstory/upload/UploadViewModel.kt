package com.elthobhy.islamicstory.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.core.utils.vo.Resource
import java.io.File

class UploadViewModel(private val userCase: UseCase) : ViewModel() {
    fun postDataNabi(
        nama: String? = null,
        umur: String? = null,
        tempatDiutus: String? = null,
        kisah: String? = null,
        keyId: String,
        profile: File? = null,
        display: File? = null,
        recentActivity: Boolean,
        tag: String
        ): LiveData<Resource<ListDomain>> = userCase.postDataNabi(nama, umur, tempatDiutus, kisah, keyId, profile, display, recentActivity,tag)
    suspend fun removeData(keyId: String?): LiveData<Resource<String>>? = keyId?.let { userCase.removeData(it) }

}