package com.example.kuitandroidapiexample

import android.app.Application

class App: Application() {
    val appContainer by lazy { AppContainer() }
}