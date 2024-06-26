package goball.uz.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import goball.uz.cache.AppCache

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCache.init(this)
        instance = this
    }

    companion object {
        var instance: Application? = null
            private set
    }
}