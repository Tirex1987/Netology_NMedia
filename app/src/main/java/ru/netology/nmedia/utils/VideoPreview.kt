package ru.netology.nmedia.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.netology.nmedia.R

internal fun ImageView.setVideoPreview(url: String) {
    Picasso.get()
        .load(VideoPreview.getImageString(url))
        .placeholder(R.drawable.default_preview)
        .into(this)
}

object VideoPreview {
    private const val YOUTUBE_FULL_URL = "https://www.youtube.com/watch?v="
    private const val YOUTUBE_SHORT_URL = "https://youtu.be/"
    private const val YOUTUBE_IMAGE_URL = "https://img.youtube.com/vi/"
    private const val YOUTUBE_IMAGE_RESOLUTION_HQ = "/hqdefault.jpg"
    private const val DUMMY = "https://"

    private fun getVideoUnicalString(urlVideo: String) : String? {
        val indexFull = urlVideo.indexOf(YOUTUBE_FULL_URL, 0, true)
        if (indexFull >= 0) {
            return urlVideo.substring(indexFull + YOUTUBE_FULL_URL.length)
        }
        val indexShort = urlVideo.indexOf(YOUTUBE_SHORT_URL, 0, true)
        if (indexShort >= 0) {
            return urlVideo.substring(indexShort + YOUTUBE_SHORT_URL.length)
        }
        return null
    }

     fun getImageString(urlVideo: String) : String {
        val videoUnicalString = getVideoUnicalString(urlVideo) ?: return DUMMY
        return YOUTUBE_IMAGE_URL + videoUnicalString + YOUTUBE_IMAGE_RESOLUTION_HQ
    }
}