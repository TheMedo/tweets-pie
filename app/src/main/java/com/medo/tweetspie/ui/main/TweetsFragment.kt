package com.medo.tweetspie.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseFragment
import com.medo.tweetspie.di.viewModel
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.fragment_tweets.*

class TweetsFragment : BaseFragment() {

    override val viewModel by viewModel<TweetsViewModel>()

    override fun getLayoutId() = R.layout.fragment_tweets

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(context)
//        recycler.adapter =

        viewModel.tweets.observe(this, Observer<List<Tweet>>(this::showTweets))
        viewModel.fetch()
    }

    private fun showTweets(tweets: List<Tweet>) {
        println("HERE + ${tweets.size}")
    }
}