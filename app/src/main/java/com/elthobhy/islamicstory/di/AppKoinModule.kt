package com.elthobhy.islamicstory.di

import com.elthobhy.islamicstory.changepassword.ChangePasswordViewModel
import com.elthobhy.islamicstory.core.domain.usecase.RepositoryInteract
import com.elthobhy.islamicstory.core.domain.usecase.UseCase
import com.elthobhy.islamicstory.forgotpassword.ForgotPasswordViewModel
import com.elthobhy.islamicstory.main.ListViewModel
import com.elthobhy.islamicstory.login.LoginViewModel
import com.elthobhy.islamicstory.register.RegisterViewModel
import com.elthobhy.islamicstory.search.SearchViewModel
import com.elthobhy.islamicstory.upload.UploadViewModel
import com.elthobhy.islamicstory.user.UserViewModel
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
    single { UserViewModel(get()) }
    single { ChangePasswordViewModel(get()) }
    single { ForgotPasswordViewModel(get()) }
    single { ListViewModel(get()) }
    single { UploadViewModel(get()) }
    single { SearchViewModel(get()) }
}