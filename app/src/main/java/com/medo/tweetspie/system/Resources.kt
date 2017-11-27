package com.medo.tweetspie.system

import android.content.Context
import android.support.annotation.StringRes

interface Resources {

    fun getString(@StringRes stringId: Int): String
}

class ResourcesImpl(private val context: Context) : Resources {

    override fun getString(@StringRes stringId: Int): String = context.getString(stringId)
}