package com.elthobhy.islamicstory.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val useCase: UseCase): ViewModel() {
    val queryChannel = MutableStateFlow("")

    fun searchResult(tag: String? = null) =
       queryChannel
            .debounce(300)
            .distinctUntilChanged()
            .filter {
                it.trim().isNotEmpty()
            }
            .flatMapLatest {
                useCase.getSearch(it, tag)
            }.asLiveData()

}