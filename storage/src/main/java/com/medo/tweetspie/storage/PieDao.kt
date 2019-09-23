package com.medo.tweetspie.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.medo.tweetspie.storage.model.Pie
import com.medo.tweetspie.storage.model.PieFriend
import com.medo.tweetspie.storage.model.PieMedia
import com.medo.tweetspie.storage.model.RawPie

@Dao
interface PieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pies: List<Pie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedia(pies: List<PieMedia>)

    @Transaction
    @Query("SELECT * FROM pies ORDER BY score DESC LIMIT 30")
    fun getPies(): LiveData<List<RawPie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriends(friends: List<PieFriend>)

    @Query("SELECT * FROM friends WHERE friendId = :userId")
    fun getFriend(userId: String): PieFriend?

    @Query("DELETE FROM pies where pieId NOT IN (SELECT pieId from pies ORDER BY createdAt DESC LIMIT 400)")
    fun deleteOldData()

    @Query("UPDATE pies SET retweeted=1, retweetCount = retweetCount + 1 WHERE pieId = :id")
    fun retweet(id: Long)

    @Query("UPDATE pies SET retweeted=0, retweetCount = retweetCount - 1 WHERE pieId = :id")
    fun unretweet(id: Long)

    @Query("UPDATE pies SET favorited=1, favoriteCount = favoriteCount + 1 WHERE pieId = :id")
    fun favorite(id: Long)

    @Query("UPDATE pies SET favorited=0, favoriteCount = favoriteCount - 1 WHERE pieId = :id")
    fun unfavorite(id: Long)
}