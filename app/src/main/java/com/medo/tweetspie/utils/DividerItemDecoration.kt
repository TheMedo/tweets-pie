package com.medo.tweetspie.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val divider: Drawable?

    init {
        val dividerAttrs = intArrayOf(android.R.attr.listDivider)
        val typedArray = context.obtainStyledAttributes(dividerAttrs)
        divider = typedArray.getDrawable(0)
        typedArray.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        divider ?: return
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        divider ?: return
        outRect.set(0, 0, 0, divider.intrinsicHeight)
    }
}