package com.aljebrastudio.islamicstory.core.di

import com.aljebrastudio.islamicstory.core.data.Repository
import com.aljebrastudio.islamicstory.core.data.remote.RemoteDataSource
import com.aljebrastudio.islamicstory.core.domain.repository.RepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val repository = module {
    val firebaseAuth = FirebaseAuth.getInstance()
    val user =
        FirebaseDatabase.getInstance("https://islamic-story-01-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference(
                "users"
            )

    single { RemoteDataSource(firebaseAuth, user) }
    single<RepositoryInterface> { Repository(get()) }
}