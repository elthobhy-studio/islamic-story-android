package com.aljebrastudio.islamicstory.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aljebrastudio.islamicstory.core.domain.model.ListDomain
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase

class ListViewModel(private val useCase: UseCase): ViewModel() {
    fun getData(): LiveData<List<ListDomain>> = useCase.getData()
}