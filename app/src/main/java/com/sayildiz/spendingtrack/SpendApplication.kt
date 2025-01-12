package com.sayildiz.spendingtrack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpendApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
