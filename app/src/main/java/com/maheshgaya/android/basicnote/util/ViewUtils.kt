package com.maheshgaya.android.basicnote.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.support.design.widget.Snackbar
import android.text.Html
import android.text.Spanned
import android.view.View
import com.maheshgaya.android.basicnote.ui.auth.AuthActivity

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
fun String.fromHtml(mode: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, mode)
    } else {
        Html.fromHtml(this)
    }

}

/**
 * convert Spanned to Html
 * @param span HTML in Spanned format
 * @return html in String format
 */
fun Spanned.toHtml(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.toHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.toHtml(this)
    }
}

 fun Activity.openAuthActivity() {
     val intent = Intent(this, AuthActivity::class.java)
     intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
     startActivity(intent)
     finish()
}

fun View.showSnackbar(message:String,
                      duration:Int = Snackbar.LENGTH_SHORT, @ColorInt color: Int = android.R.color.white){
    var time = Snackbar.LENGTH_SHORT

    if (duration == Snackbar.LENGTH_LONG || duration == Snackbar.LENGTH_INDEFINITE) {
        time = duration
    }
    val colorRes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.context.getColor(color)
    } else {
        this.context.resources.getColor(color)
    }
    Snackbar.make(this, message, time).setActionTextColor(colorRes) .show()

}