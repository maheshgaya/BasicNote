package com.maheshgaya.android.basicnote.util

import android.app.Activity
import android.content.Intent
import android.support.annotation.IdRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.ui.search.SearchActivity
import android.text.Html
import android.text.Spanned
import android.util.Log


/**
 * Created by Mahesh Gaya on 8/12/17.
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
 * validates email
 * @return true if email is valid
 */
fun String.validateEmail(): Boolean = this.matches(Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))

/**
 * validates the password
 * @return True if password is valid
 */
fun String.validatePassword(): Boolean = this.matches(Regex("^[a-zA-Z0-9!#\$_&=]+$")) && this.length >= 8

/**
 * Convers string to CamelCase
 */
fun String.toCamelCase(): String {
    //split the string with whitespace into a String array
    val stringArray = StringBuilder(this).split(Regex("\\s+")).toMutableList()

    //convert the first character of each string
    stringArray.indices
            .asSequence()
            .filterNot { stringArray[it].isNullOrEmpty() }
            .forEach { stringArray[it] = stringArray[it][0].toUpperCase().toString() + stringArray[it].subSequence(1, stringArray[it].lastIndex + 1) }

    //build the string back with CamelCase
    val retBuilder = StringBuilder()
    for (i in stringArray.indices) {
        if (i != stringArray.lastIndex) {
            retBuilder.append(stringArray[i] + " ")
        } else {
            retBuilder.append(stringArray[i])
        }
    }
    return retBuilder.toString()

}

/**
 * Signs the user out
 */
fun signOut() {
    FirebaseAuth.getInstance().signOut()
}


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
        result = Html.fromHtml(html.toString())
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