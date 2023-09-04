package com.elthobhy.islamicstory.core.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors @VisibleForTesting constructor(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor
) {
    companion object{
        private const val THREAD_COUNT = 3
    }

    constructor(): this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(THREAD_COUNT),
        MainThreadExecutors()
    )

    fun diskIO(): Executor = diskIO

    fun networkIO(): Executor = networkIO

    fun mainThread(): Executor = mainThread

    class MainThreadExecutors : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(p0: Runnable) {
            mainThreadHandler.post(p0)
        }
    }
}
