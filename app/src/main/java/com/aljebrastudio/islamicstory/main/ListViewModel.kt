package com.aljebrastudio.islamicstory.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.core.utils.vo.Resource

class ListViewModel(private val useCase: UseCase): ViewModel() {
    fun getData(): LiveData<Resource<List<ListDomain>>> = useCase.getData()
}