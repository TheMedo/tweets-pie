package com.medo.tweetspie.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseActivity
import com.medo.tweetspie.di.Context
import com.medo.tweetspie.extensions.launchActivity
import com.medo.tweetspie.extensions.toast
import com.medo.tweetspie.onboarding.OnboardingActivity
import com.medo.tweetspie.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

private const val REQUEST_CODE_ONBOARDING = 420

class MainActivity : BaseActivity(), MainContract.View {

    override val contextName: String = Context.Main
    override val presenter by inject<MainContract.Presenter>()

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

    override fun startOnboarding() = launchActivity<OnboardingActivity>(REQUEST_CODE_ONBOARDING)

    override fun loadData() {
        progress.visibility = View.VISIBLE
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
