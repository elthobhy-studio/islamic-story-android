package com.elthobhy.islamicstory

import android.app.Application
import com.elthobhy.islamicstory.core.di.database
import com.elthobhy.islamicstory.core.di.repository
import com.elthobhy.islamicstory.di.adapter
import com.elthobhy.islamicstory.di.useCase
import com.elthobhy.islamicstory.di.viewModel
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
                adapter,
                database
            )
        }
    }
}