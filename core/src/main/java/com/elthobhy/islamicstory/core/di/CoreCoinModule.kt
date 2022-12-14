package com.elthobhy.islamicstory.core.di

import com.elthobhy.islamicstory.core.data.Repository
import com.elthobhy.islamicstory.core.data.remote.RemoteDataSource
import com.elthobhy.islamicstory.core.domain.repository.RepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val repository = module {
    val firebaseAuth = FirebaseAuth.getInstance()
    val user =
        FirebaseDatabase.getInstance("https://islamic-story-01-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference(
                "users"
            )
    val dataNabi =
        FirebaseDatabase.getInstance("https://islamic-story-01-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference(
                "dataNabi"
            )
    val ref =
        FirebaseStorage.getInstance("gs://islamic-story-01.appspot.com")
            .getReference(
                "images"
            )
    single { RemoteDataSource(firebaseAuth, user, dataNabi, ref) }
    single<RepositoryInterface> { Repository(get()) }
}