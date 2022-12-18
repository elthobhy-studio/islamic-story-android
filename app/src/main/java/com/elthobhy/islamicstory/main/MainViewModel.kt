package com.elthobhy.islamicstory.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase

class MainViewModel(private val useCase: UseCase): ViewModel() {
    fun getRecentActivity(): LiveData<List<ListDomain>> = useCase.getRecentActivity().asLiveData()
}