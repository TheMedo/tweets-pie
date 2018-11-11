package com.medo.tweetspie.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.medo.tweetspie.R
import com.medo.tweetspie.base.BaseFragment
import com.medo.tweetspie.extensions.openUrl
import com.medo.tweetspie.extensions.show
import kotlinx.android.synthetic.main.fragment_tweets.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PiesFragment : BaseFragment() {

    override val viewModel by viewModel<PiesViewModel>()

    private val adapter by lazy { PieAdapter(viewModel) }

    override fun getLayoutId() = R.layout.fragment_tweets

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        fab.setOnClickListener { viewModel.refresh.postValue(false) }

        viewModel.pies.observe(this, Observer<List<BakedPie>>(this::showPies))
        viewModel.loading.observe(this, Observer<Boolean>(this::showLoading))
        viewModel.refresh.observe(this, Observer<Boolean>(this::showRefresh))
        viewModel.userAction.observe(this, Observer(this::openUser))
        viewModel.tweetAction.observe(this, Observer(this::openTweet))
    }

    private fun showPies(pies: List<BakedPie>) {
        val hasData = adapter.itemCount > 0
        viewModel.refresh.postValue(hasData)

        adapter.setData(pies)
        if (!hasData) adapter.notifyDataSetChanged()
    }

    private fun showLoading(show: Boolean) {
        progress.show(visible = show)
    }

    private fun showRefresh(show: Boolean) {
        if (show) fab.show() else fab.hide()
        if (!show) adapter.notifyDataSetChanged()
    }

    private fun openUser(userUrl: String) {
        activity?.openUrl(userUrl)
    }

    private fun openTweet(tweetUrl: String) {
        activity?.openUrl(tweetUrl)
    }
}