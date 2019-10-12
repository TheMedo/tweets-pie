package com.medo.tweetspie.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.medo.tweetspie.R
import com.medo.tweetspie.util.base.BaseFragment
import com.medo.tweetspie.util.extensions.openUrl
import com.medo.tweetspie.util.extensions.show
import kotlinx.android.synthetic.main.fragment_tweets.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PiesFragment : BaseFragment() {

    private val adapter by lazy { PieAdapter(viewModel) }

    override val viewModel by viewModel<PiesViewModel>()

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

        fab.setOnClickListener {
            viewModel.persistTweets()
            fab.hide()
        }

        viewModel.pies.observe(this, Observer<List<BakedPie>>(this::showPies))
        viewModel.loading.observe(this, Observer<Boolean>(this::showLoading))
        viewModel.urlAction.observe(this, Observer(this::openUrl))
    }

    private fun showPies(pies: List<BakedPie>) {
        adapter.setData(pies)
    }

    private fun showLoading(show: Boolean) {
        progress.show(visible = show)
        if (!show) fab.show()
    }

    private fun openUrl(url: String) {
        activity?.openUrl(url)
    }
}