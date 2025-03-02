package online.nyam.hikka

import android.app.Application
import online.nyam.hikka.di.apiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HikkaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HikkaApplication)
            modules(apiModule)
        }
    }
}
