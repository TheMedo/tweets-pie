package com.medo.tweetspie.base

import android.support.v7.app.AppCompatActivity
import org.koin.standalone.releaseContext

abstract class BaseActivity : AppCompatActivity(), BaseView {

    abstract val contextName: String
    abstract val presenter: BasePresenter<*>

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
        releaseContext(contextName)
    }
}