package com.example.project

import android.app.Application

class PlatformGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserRepository.initialize(this)
    }
}