package com.medo.tweetspie.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.medo.tweetspie.util.extensions.snack

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel?

    abstract fun getLayoutId(): Int

    private val failureObserver = Observer<String?> { activity?.snack(it) }

    override fun onResume() {
        super.onResume()
        viewModel?.failure?.observe(this, failureObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel?.failure?.removeObserver(failureObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(getLayoutId(), container, false)
}