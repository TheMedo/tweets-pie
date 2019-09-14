package com.medo.tweetspie.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

const val KEY_HANDLE = "keyHandle"

interface UserRepository {

    fun getUser(): LiveData<String>

    fun setUser(handle: String)
}

class UserRepositoryImpl(
    private val prefs: SharedPreferences
) : UserRepository {

    private val user = MutableLiveData<String>()

    init {
        user.postValue(prefs.getString(KEY_HANDLE, null))
    }

    override fun getUser(): LiveData<String> = user

    override fun setUser(handle: String) {
        prefs.edit { putString(KEY_HANDLE, handle) }
        user.postValue(handle)
    }
}