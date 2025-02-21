package com.example.algorithms

import android.app.Application
import com.example.algorithms.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AlgorithmsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Проверяем, не был ли Koin уже инициализирован
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(this@AlgorithmsApplication)
                modules(appModule)
            }
        }
    }
}