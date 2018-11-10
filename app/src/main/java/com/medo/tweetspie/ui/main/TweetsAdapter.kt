package com.medo.tweetspie.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.medo.tweetspie.R
import kotlinx.android.synthetic.main.item_pie.view.*

class PieAdapter : RecyclerView.Adapter<PieViewHolder>() {

    private var data = emptyList<String>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PieViewHolder =
        PieViewHolder(parent.inflate(R.layout.item_pie))

    override fun onBindViewHolder(holder: PieViewHolder, position: Int) {
        val item = data[position]
        holder.setTweet(item)
    }

    fun setData(items: List<String>) {
        data = items
    }
}

class PieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun setTweet(text: String) {
        itemView.text_tweet.text = text
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)