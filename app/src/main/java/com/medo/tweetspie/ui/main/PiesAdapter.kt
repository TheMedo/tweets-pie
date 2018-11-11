package com.medo.tweetspie.ui.main

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.medo.tweetspie.R
import com.medo.tweetspie.extensions.show
import com.medo.tweetspie.glide.GlideApp
import kotlinx.android.synthetic.main.item_pie.view.*

data class BakedPie(
    val userAvatarUrl: String?,
    val userName: String,
    val userHandle: String,
    val userProtected: Boolean,
    val userVerified: Boolean,
    val timestamp: String,
    val info: String?,
    val text: String,
    val retweeted: Boolean,
    val retweetCount: String,
    val favorited: Boolean,
    val favoriteCount: String,
    val score: String,
    val userUrl: String,
    val tweetUrl: String
)

class PieAdapter(
    private val viewModel: PiesViewModel
) : RecyclerView.Adapter<PieViewHolder>() {

    private var data = emptyList<BakedPie>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PieViewHolder =
        PieViewHolder(parent.inflate(R.layout.item_pie), viewModel)

    override fun onBindViewHolder(holder: PieViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    fun setData(items: List<BakedPie>) {
        data = items
    }
}

class PieViewHolder(
    view: View,
    private val viewModel: PiesViewModel
) : RecyclerView.ViewHolder(view) {

    init {
        val flags = itemView.text_timestamp.paintFlags
        itemView.text_timestamp.paintFlags = flags or Paint.UNDERLINE_TEXT_FLAG
    }

    fun bindData(item: BakedPie) {
        GlideApp.with(itemView)
            .load(item.userAvatarUrl)
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .centerCrop()
            .apply(RequestOptions.circleCropTransform())
            .into(itemView.image_avatar)
        itemView.text_name.text = item.userName
        itemView.text_handle.text = item.userHandle
        itemView.text_timestamp.text = item.timestamp
        itemView.image_verified.show(visible = item.userVerified)
        itemView.image_protected.show(visible = item.userProtected)
        itemView.text_info.text = item.info
        itemView.text_tweet.text = item.text
        itemView.button_retweet.isChecked = item.retweeted
        itemView.button_retweet.text = item.retweetCount
        itemView.button_favorite.isChecked = item.favorited
        itemView.button_favorite.text = item.favoriteCount
        itemView.text_score.text = item.score

        val userClickListener: (View) -> Unit = { viewModel.userAction.post(item.userUrl) }
        itemView.image_avatar.setOnClickListener(userClickListener)
        itemView.text_name.setOnClickListener(userClickListener)
        itemView.text_handle.setOnClickListener(userClickListener)
        itemView.text_timestamp.setOnClickListener { viewModel.tweetAction.post(item.tweetUrl) }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)