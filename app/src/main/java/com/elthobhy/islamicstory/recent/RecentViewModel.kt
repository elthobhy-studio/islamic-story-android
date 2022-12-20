package com.elthobhy.islamicstory.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase

class RecentViewModel(private val useCase: UseCase): ViewModel() {
    fun getRecentActivity(): LiveData<List<ListDomain>> = useCase.getRecentActivity().asLiveData()
    fun clearRecentActivity(keyId: String, story: ListDomain) = useCase.clearRecentActivity(false, keyId, story)
}