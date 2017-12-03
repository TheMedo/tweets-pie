package com.medo.tweetspie.service


import com.medo.tweetspie.base.BasePresenter
import com.medo.tweetspie.base.BaseView


interface ServiceMvp {

    interface View : BaseView {

        fun showErrorNotification()

        fun sendUpdateBroadcast(success: Boolean)
    }

    interface Presenter : BasePresenter<View>
}
