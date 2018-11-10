package com.medo.tweetspie.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pies")
data class Pie(
    @PrimaryKey
    var pieId: String = "",

    var text: String = "",
    var createdAt: String = "",
    var favoriteCount: Int = 0,
    var favorited: Boolean = false,
    var retweetCount: Int = 0,
    var retweeted: Boolean = false,

    var language: String = "",
    var country: String? = null,
    var source: String? = null,

    var inReplyTo: String? = null,
    var retweetedBy: String? = null,
    var quoted: Boolean = false,

    @Embedded
    var user: PieUser,

    var score: Int = 0
)

@Entity(tableName = "users")
data class PieUser(
    @PrimaryKey
    var userId: String = "",
    var name: String = "",
    var avatarUrl: String = "",
    var handle: String = "",
    var protected: Boolean = false,
    var verified: Boolean = false,
    var followingCount: Int = 0,
    var followersCount: Int = 0,
    var tweetsCount: Int = 0,
    var interactionCount: Long = 0,
    var friends: Boolean = false
)

@Entity(tableName = "friends")
data class PieFriend(
    @PrimaryKey
    var friendId: String = ""
)