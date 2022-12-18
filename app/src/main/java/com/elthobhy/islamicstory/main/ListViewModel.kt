package com.elthobhy.islamicstory.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.core.utils.vo.Resource

class ListViewModel(private val useCase: UseCase): ViewModel() {
    fun getData() = useCase.getData().asLiveData()
}