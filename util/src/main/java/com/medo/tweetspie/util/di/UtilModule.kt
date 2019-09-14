package com.medo.tweetspie.util.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

const val DISPATCHER_MAIN = "MAIN"
const val DISPATCHER_IO = "IO"

val utilModule = module {
    single(StringQualifier(DISPATCHER_IO)) { Dispatchers.IO }
    single(StringQualifier(DISPATCHER_MAIN)) { Dispatchers.Main }
}