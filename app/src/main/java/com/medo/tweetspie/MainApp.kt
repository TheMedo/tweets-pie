package com.medo.tweetspie

import android.app.Application
import com.medo.tweetspie.di.mainModule
import com.medo.tweetspie.remote.Remote
import com.medo.tweetspie.remote.di.remoteModule
import com.medo.tweetspie.storage.di.storageModule
import com.medo.tweetspie.system.di.systemModule
import com.medo.tweetspie.util.di.utilModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // init DI
        startKoin {
            androidContext(this@MainApp)
            modules(listOf(mainModule, systemModule, utilModule, storageModule, remoteModule))
        }
        // init remote
        Remote.init(this)
    }
}
