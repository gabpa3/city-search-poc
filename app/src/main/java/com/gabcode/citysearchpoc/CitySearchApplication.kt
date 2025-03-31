package com.gabcode.citysearchpoc

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CitySearchApplication : Application() {

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // TODO Log by levels
    }
}
