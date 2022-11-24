package com.aljebrastudio.islamicstory.core.di

import com.aljebrastudio.islamicstory.core.data.Repository
import com.aljebrastudio.islamicstory.core.data.remote.RemoteDataSource
import com.aljebrastudio.islamicstory.core.domain.repository.RepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val repository = module {
    val firebaseAuth = FirebaseAuth.getInstance()
    single { RemoteDataSource(firebaseAuth) }
    single<RepositoryInterface> { Repository(get()) }
}