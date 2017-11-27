package com.medo.tweetspie.main

import com.medo.tweetspie.R
import com.medo.tweetspie.base.AbsBasePresenter
import com.medo.tweetspie.system.Preferences
import com.medo.tweetspie.system.Resources
import com.medo.tweetspie.system.USERNAME

class MainPresenter(
        private val preferences: Preferences,
        private val resources: Resources
) : AbsBasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun onAttach(view: MainContract.View) {
        super.onAttach(view)
        if (!preferences.has(USERNAME)) view.startOnboarding()
        else view.loadData()
    }

    override fun onOnboardingSuccess() {
        view?.loadData()
    }

    override fun onOnboardingFailure() {
        view?.exit()
    }

    override fun onDataLoaded(success: Boolean) {
        if (success) view?.showData(emptyList(), false)
        else view?.showError(resources.getString(R.string.error_load_data_failure))
    }
}