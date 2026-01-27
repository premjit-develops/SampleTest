package com.sampletest

import android.app.Application
import com.sampletest.shared.di.initKoin
import org.koin.android.ext.koin.androidContext

class SampleTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SampleTestApplication)
        }
    }
}