package com.elthobhy.islamicstory.core.di

import androidx.room.Room
import com.elthobhy.islamicstory.core.data.Repository
import com.elthobhy.islamicstory.core.data.local.LocalDataSource
import com.elthobhy.islamicstory.core.data.local.room.DatabaseLocal
import com.elthobhy.islamicstory.core.data.remote.RemoteDataSource
import com.elthobhy.islamicstory.core.domain.repository.RepositoryInterface
import com.elthobhy.islamicstory.core.ui.AdapterList
import com.elthobhy.islamicstory.core.utils.AppExecutors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
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
    single<RepositoryInterface> { Repository(get(), get(), get()) }
    single { LocalDataSource(get()) }
    factory { AppExecutors() }
}
val database = module {
    factory { get<DatabaseLocal>().dao() }
    single {
        val passphrase : ByteArray = SQLiteDatabase.getBytes("passphrase".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            DatabaseLocal::class.java, "story_db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}