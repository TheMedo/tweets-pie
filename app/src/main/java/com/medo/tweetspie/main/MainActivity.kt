package com.medo.tweetspie.main

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseActivity
import com.medo.tweetspie.di.BindContext
import com.medo.tweetspie.extensions.launchActivity
import com.medo.tweetspie.extensions.register
import com.medo.tweetspie.extensions.toast
import com.medo.tweetspie.extensions.unregister
import com.medo.tweetspie.onboarding.OnboardingActivity
import com.medo.tweetspie.service.MainService
import com.medo.tweetspie.utils.ACTION_UPDATE
import com.medo.tweetspie.utils.DividerItemDecoration
import com.medo.tweetspie.utils.EXTRA_SUCCESS
import com.medo.tweetspie.utils.REQUEST_CODE_ONBOARDING
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity(), MainMvp.View {

    override val contextName: String = BindContext.Main
    override val presenter by inject<MainMvp.Presenter>()

    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            presenter.onDataLoaded(intent.getBooleanExtra(EXTRA_SUCCESS, false))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onAttach(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE_ONBOARDING) return
        if (resultCode == Activity.RESULT_OK) presenter.onOnboardingSuccess()
        else presenter.onOnboardingFailure()
    }

    override fun onResume() {
        super.onResume()
        register(updateReceiver, IntentFilter(ACTION_UPDATE))
    }

    override fun onPause() {
        super.onPause()
        unregister(updateReceiver)
    }

    override fun startOnboarding() = launchActivity<OnboardingActivity>(REQUEST_CODE_ONBOARDING)

    override fun loadData() {
        progress.visibility = View.VISIBLE
        startService(Intent(this, MainService::class.java))
    }

    override fun showData(data: List<Any>, cached: Boolean) {
        if (!cached) progress.visibility = View.GONE

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.addItemDecoration(DividerItemDecoration(this))
    }

    override fun showError(message: String) = toast(message)

    override fun exit() = finish()
}
