package com.example.jetpack1

import android.app.Application
import com.example.jetpack1.datastore.PreferencesEncryptedShared
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesEncryptedShared.init(this@MyApp)
    }
}