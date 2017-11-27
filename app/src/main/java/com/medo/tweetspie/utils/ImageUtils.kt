package com.medo.tweetspie.utils


import android.graphics.Matrix
import android.view.TextureView

object ImageUtils {

    /**
     * Adjusts the aspect ratio of the given texture view, to match
     * the prepared video aspect ratio to avoid video stretching.
     *
     * @param textureView the texture view that needs adjusting
     * @param videoWidth  the prepared video width
     * @param videoHeight the prepared video height
     */
    fun adjustAspectRatio(textureView: TextureView, videoWidth: Int, videoHeight: Int) {
        val textureWidth = textureView.width
        val textureHeight = textureView.height
        val aspectRatio = videoHeight.toDouble() / videoWidth
        val newWidth: Int
        val newHeight: Int
        if (textureHeight > (textureWidth * aspectRatio).toInt()) {
            // limited by narrow width, restrict height
            newWidth = textureWidth
            newHeight = (textureWidth * aspectRatio).toInt()
        } else {
            // limited by short height, restrict width
            newWidth = (textureHeight / aspectRatio).toInt()
            newHeight = textureHeight
        }

        val xOffset = (textureWidth - newWidth) / 2
        val yOffset = (textureHeight - newHeight) / 2
        val transformMatrix = Matrix()
        textureView.getTransform(transformMatrix)
        transformMatrix.setScale(newWidth.toFloat() / textureWidth, newHeight.toFloat() / textureHeight)
        transformMatrix.postTranslate(xOffset.toFloat(), yOffset.toFloat())
        textureView.setTransform(transformMatrix)
    }
}
