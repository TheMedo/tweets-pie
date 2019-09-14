package com.medo.tweetspie.storage.di

import androidx.room.Room
import com.medo.tweetspie.storage.PieDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val storageModule = module {
    single { Room.databaseBuilder(androidApplication(), PieDatabase::class.java, "pie_db").build() }
    single { get<PieDatabase>().pieDao() }
}