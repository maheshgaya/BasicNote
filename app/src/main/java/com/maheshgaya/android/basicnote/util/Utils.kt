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

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
fun <T : View> Activity.bind(@IdRes res: Int): T = findViewById(res)

fun <T : View> View.bind(@IdRes res: Int): T = this.findViewById(res)

fun String.validateEmail(): Boolean = this.matches(Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"))

fun String.validatePassword(): Boolean = this.matches(Regex("^[a-zA-Z0-9!#\$_&=]+$")) && this.length >= 8

fun String.toCamelCase(): String {
    val stringArray = StringBuilder(this).split(Regex("\\s+")).toMutableList()

    stringArray.indices
            .asSequence()
            .filterNot { stringArray[it].isNullOrEmpty() }
            .forEach { stringArray[it] = stringArray[it][0].toUpperCase().toString() + stringArray[it].subSequence(1, stringArray[it].lastIndex + 1) }

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

fun signOut() {
    FirebaseAuth.getInstance().signOut()
}