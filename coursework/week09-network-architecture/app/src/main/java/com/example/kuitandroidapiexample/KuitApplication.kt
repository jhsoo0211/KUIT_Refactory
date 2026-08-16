package com.example.kuitandroidapiexample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KuitApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}