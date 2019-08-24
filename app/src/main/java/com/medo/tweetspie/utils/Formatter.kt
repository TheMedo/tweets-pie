package com.medo.tweetspie.utils

import android.content.Context
import android.text.format.DateUtils
import android.text.util.Linkify
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

interface Formatter {

    /**
     * Converts the UTC time string into millis.
     *
     * @param utcTime the UTC time in "EEE MMM dd HH:mm:ss z yyyy" format
     * @return the [Date] or null if the string cannot be parsed
     */
    fun utcToDate(utcTime: String): Date

    /**
     * Formats the UTC time string into a relative time span string.
     *
     * @param date the date to be formatted
     * @return the relative time string or empty if UTC time cannot be parsed
     */
    fun toRelativeDate(date: Date?): String

    /**
     * Convenient method for adding clickable links to @mentions, #hashtags and urls
     * for the text set into the TextView
     *
     * @param target the [TextView] to [Linkify]
     */
    fun addLinks(target: TextView)

    /**
     * Formats large numbers with suffix format K, M, G
     *
     * @param number the number to format
     * @return the formatted number
     */
    fun formatNumber(number: Long): String

    fun getTweetUrl(id: String, screenName: String): String

    fun getUserUrl(screenName: String): String
}

class FormatterImpl(val context: Context) : Formatter {

    private val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
    private val mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)")
    private val mentionScheme = "http://www.twitter.com/"
    private val hashTagPattern = Pattern.compile("#([A-Za-z0-9_-]+)")
    private val hastTagScheme = "http://www.twitter.com/hashtag/"
    private val urlPattern = Pattern.compile("[a-z]+://[^ \\n]*")
    private val filter = Linkify.TransformFilter { match, _ -> match.group() }
    private val suffixes = charArrayOf('K', 'M', 'G')
    private val userFormat = "http://twitter.com/%s/"
    private val urlFormat = "http://twitter.com/%s/status/%s"

    override fun utcToDate(utcTime: String): Date = try {
        dateFormat.parse(utcTime) ?: Date()
    } catch (e: ParseException) {
        Date()
    }

    override fun toRelativeDate(date: Date?): String = when (date) {
        null -> ""
        else -> DateUtils.getRelativeTimeSpanString(
            date.time,
            System.currentTimeMillis(),
            TimeUnit.SECONDS.toMillis(1),
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }

    override fun addLinks(target: TextView) {
        Linkify.addLinks(target, mentionPattern, mentionScheme, null, filter)
        Linkify.addLinks(target, hashTagPattern, hastTagScheme, null, filter)
        Linkify.addLinks(target, urlPattern, null, null, filter)
    }

    override fun formatNumber(number: Long): String {
        if (number < 1000) {
            return number.toString()
        }

        val string = number.toString()
        val magnitude = (string.length - 1) / 3
        val digits = (string.length - 1) % 3 + 1
        val value = CharArray(4)
        for (i in 0 until digits) {
            value[i] = string[i]
        }
        var valueLength = digits
        if (digits == 1 && string[1] != '0') {
            value[valueLength++] = '.'
            value[valueLength++] = string[1]
        }
        value[valueLength++] = suffixes[magnitude - 1]
        return String(value, 0, valueLength)
    }

    override fun getTweetUrl(id: String, screenName: String) =
        String.format(urlFormat, screenName, id)

    override fun getUserUrl(screenName: String) = String.format(userFormat, screenName)
}