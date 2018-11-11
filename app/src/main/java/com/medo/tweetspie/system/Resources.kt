package com.medo.tweetspie.system

import android.content.Context
import androidx.annotation.StringRes

interface Resources {

    fun getString(@StringRes id: Int = 0, vararg args: String = emptyArray()): String
}

class ResourcesImpl(
    private val context: Context
) : Resources {

    override fun getString(@StringRes id: Int, vararg args: String) =
        context.getString(id, *args) ?: ""
}