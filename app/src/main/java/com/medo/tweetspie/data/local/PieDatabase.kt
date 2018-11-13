package com.medo.tweetspie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.local.model.PieFriend
import com.medo.tweetspie.data.local.model.PieMedia
import com.medo.tweetspie.data.local.model.PieUser

@Database(entities = [Pie::class, PieUser::class, PieFriend::class, PieMedia::class], version = 1)
abstract class PieDatabase : RoomDatabase() {

    abstract fun pieDao(): PieDao
}