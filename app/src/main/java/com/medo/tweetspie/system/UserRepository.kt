package com.medo.tweetspie.system

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

const val KEY_USERNAME = "keyUsername"

interface UserRepository {

    fun getUser(): LiveData<String>

    fun setUser(username: String)
}

class UserRepositoryImpl(
        private val prefs: SharedPreferences
) : UserRepository {

    private val user = MutableLiveData<String>()

    init {
        user.postValue(prefs.getString(KEY_USERNAME, null))
    }

    override fun getUser(): LiveData<String> = user

    override fun setUser(username: String) {
        prefs.edit { putString(KEY_USERNAME, username) }
        user.postValue(username)
    }
}