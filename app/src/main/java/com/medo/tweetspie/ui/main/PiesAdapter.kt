package com.medo.tweetspie.ui.main

import android.graphics.Paint
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.medo.tweetspie.R
import com.medo.tweetspie.glide.GlideApp
import com.medo.tweetspie.util.extensions.show
import kotlinx.android.synthetic.main.item_pie.view.*

data class BakedPie(
    val id: String,
    val userAvatarUrl: String?,
    val userName: String,
    val userHandle: String,
    val userProtected: Boolean,
    val userVerified: Boolean,
    val timestamp: String,
    val info: String?,
    val text: Spanned,
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
        if (data.isEmpty()) {
            data = items
            notifyItemChanged(0, items.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize() = data.size

                override fun getNewListSize() = items.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    data[oldItemPosition].id == items[newItemPosition].id

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    data[oldItemPosition].retweeted == items[newItemPosition].retweeted &&
                            data[oldItemPosition].retweetCount == items[newItemPosition].retweetCount &&
                            data[oldItemPosition].favorited == items[newItemPosition].favorited &&
                            data[oldItemPosition].favoriteCount == items[newItemPosition].favoriteCount
            })
            data = items
            result.dispatchUpdatesTo(this)
        }
    }
}

class PieViewHolder(
    view: View,
    private val viewModel: PiesViewModel
) : RecyclerView.ViewHolder(view) {

    init {
        itemView.text_tweet.movementMethod = LinkMovementMethod.getInstance()

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

        val userClickListener: (View) -> Unit = { viewModel.urlAction.post(item.userUrl) }
        itemView.image_avatar.setOnClickListener(userClickListener)
        itemView.text_name.setOnClickListener(userClickListener)
        itemView.text_handle.setOnClickListener(userClickListener)
        itemView.text_timestamp.setOnClickListener { viewModel.urlAction.post(item.tweetUrl) }
        itemView.button_retweet.setOnClickListener { viewModel.retweet(item.id, item.retweeted) }
        itemView.button_favorite.setOnClickListener { viewModel.favorite(item.id, item.favorited) }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)