package com.medo.tweetspie.util.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.medo.tweetspie.util.extensions.snack

abstract class BaseActivity : AppCompatActivity() {

    abstract val viewModel: BaseViewModel?

    private val failureObserver = Observer<String?> { snack(it) }

    override fun onResume() {
        super.onResume()
        viewModel?.failure?.observe(this, failureObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel?.failure?.removeObserver(failureObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}