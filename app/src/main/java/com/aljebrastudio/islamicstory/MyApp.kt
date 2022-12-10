package com.aljebrastudio.islamicstory

import android.app.Application
import com.aljebrastudio.islamicstory.core.di.repository
import com.aljebrastudio.islamicstory.di.adapter
import com.aljebrastudio.islamicstory.di.useCase
import com.aljebrastudio.islamicstory.di.viewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@FlowPreview
@ExperimentalCoroutinesApi
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApp)
            modules(
                repository,
                viewModel,
                useCase,
                adapter
            )
        }
    }
}