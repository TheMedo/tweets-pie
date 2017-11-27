package com.medo.tweetspie.base


interface BasePresenter<in T : BaseView> {

    fun onAttach(view: T)

    fun onDetach()
}

abstract class AbsBasePresenter<T : BaseView> : BasePresenter<T> {

    protected var view: T? = null

    override fun onAttach(view: T) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }
}