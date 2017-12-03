package com.medo.tweetspie.base

import android.app.IntentService
import org.koin.android.ext.android.releaseContext

abstract class BaseService(name: String) : IntentService(name), BaseView {

    abstract val contextName: String
    abstract val presenter: BasePresenter<*>

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        releaseContext(contextName)
    }
}