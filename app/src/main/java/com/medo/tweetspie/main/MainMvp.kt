package com.medo.tweetspie.main


import com.medo.tweetspie.base.BasePresenter
import com.medo.tweetspie.base.BaseView


interface MainMvp {

    interface View : BaseView {

        fun startOnboarding()

        fun loadData()

        fun showData(data: List<Any>, cached: Boolean)

        fun showError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun onOnboardingSuccess()

        fun onOnboardingFailure()

        fun onDataLoaded(success: Boolean)
    }
}
