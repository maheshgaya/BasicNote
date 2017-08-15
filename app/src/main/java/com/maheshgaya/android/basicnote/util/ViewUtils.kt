package com.maheshgaya.android.basicnote.util

import android.app.Activity
import android.support.annotation.IdRes
import android.text.Html
import android.text.Spanned
import android.view.View

/**
 * Created by Mahesh Gaya on 8/14/17.
 */
/**
 * Binds views for activity
 */
fun <T : View> Activity.bind(@IdRes res: Int): T = findViewById(res)

/**
 * Binds views (Generic)
 */
fun <T : View> View.bind(@IdRes res: Int): T = this.findViewById(res)

/**
 * convert Html to Spanned
 * @param html HTML in String format
 * @return html in Spanned format
 */
fun fromHtml(html: String, mode: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    val TAG = "fromHtml"
    val result: Spanned
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        result = Html.fromHtml(html, mode)
    } else {
        result = Html.fromHtml(html)
    }

    return result
}

/**
 * convert Spanned to Html
 * @param span HTML in Spanned format
 * @return html in String format
 */
fun toHtml(span: Spanned): String {
    val result: String
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        result = Html.toHtml(span, Html.FROM_HTML_MODE_LEGACY)
    } else {
        result = Html.toHtml(span)
    }
    return result
}
