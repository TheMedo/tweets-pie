package com.medo.tweetspie.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medo.tweetspie.storage.model.Pie
import com.medo.tweetspie.storage.model.PieFriend
import com.medo.tweetspie.storage.model.PieMedia
import com.medo.tweetspie.storage.model.PieUser

@Database(
    entities = [Pie::class, PieUser::class, PieFriend::class, PieMedia::class],
    version = 1,
    exportSchema = false
)
abstract class PieDatabase : RoomDatabase() {

    abstract fun pieDao(): PieDao
}