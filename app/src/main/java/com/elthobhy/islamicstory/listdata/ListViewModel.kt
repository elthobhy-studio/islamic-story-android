package com.elthobhy.islamicstory.listdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase

class ListViewModel(private val useCase: UseCase): ViewModel() {
    fun getListNabi(tag: String) = useCase.getListNabi(tag).asLiveData()
    fun setRecentActivity(story: ListDomain, keyId: String){
        useCase.setRecentActivity(story, true, keyId)
    }
}