package com.aljebrastudio.islamicstory.di

import com.aljebrastudio.islamicstory.core.domain.usecase.RepositoryInteract
import com.aljebrastudio.islamicstory.core.domain.usecase.UseCase
import com.aljebrastudio.islamicstory.login.LoginViewModel
import com.aljebrastudio.islamicstory.register.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

val useCase = module {
    factory<UseCase> { RepositoryInteract(get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModel = module {
    single { RegisterViewModel(get()) }
    single { LoginViewModel(get()) }
}