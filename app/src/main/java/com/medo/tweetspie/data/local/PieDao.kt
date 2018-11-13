package com.medo.tweetspie.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medo.tweetspie.data.local.model.Pie
import com.medo.tweetspie.data.local.model.PieFriend

@Dao
interface PieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pies: List<Pie>)

    @Query("SELECT * FROM pies ORDER BY score DESC LIMIT 30")
    fun getPies(): LiveData<List<Pie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriends(friends: List<PieFriend>)

    @Query("SELECT * FROM friends WHERE friendId = :userId")
    fun getFriend(userId: String): PieFriend?

    @Query("DELETE FROM pies where pieId NOT IN (SELECT pieId from pies ORDER BY createdAt DESC LIMIT 400)")
    fun deleteOldData()
}