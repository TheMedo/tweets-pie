package com.medo.tweetspie.system

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface Resources {

    fun getString(@StringRes id: Int = 0, vararg args: String = emptyArray()): String

    fun getColor(@ColorRes id: Int): Int
}

class ResourcesImpl(
    private val context: Context
) : Resources {

    override fun getString(@StringRes id: Int, vararg args: String) =
        context.getString(id, *args) ?: ""

    override fun getColor(id: Int) = context.getColor(id)
}