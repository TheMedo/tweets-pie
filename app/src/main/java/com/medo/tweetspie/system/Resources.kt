package com.medo.tweetspie.system

import android.content.Context
import androidx.annotation.StringRes

interface Resources {

    fun getString(@StringRes id: Int): String
}

class ResourcesImpl(
        private val context: Context
) : Resources {

    override fun getString(@StringRes id: Int): String = context.getString(id)
}