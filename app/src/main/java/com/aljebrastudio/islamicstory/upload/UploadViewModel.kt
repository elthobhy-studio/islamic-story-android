package com.aljebrastudio.islamicstory.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.core.utils.vo.Resource

class UploadViewModel(private val userCase: UseCase) : ViewModel() {
    fun postDataNabi(
        nama: String,
        umur: String,
        tempatDiutus: String,
        kisah: String,
        keyId: String
    ): LiveData<Resource<ListDomain>> = userCase.postDataNabi(nama, umur, tempatDiutus, kisah, keyId)
}