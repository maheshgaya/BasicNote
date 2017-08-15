package com.maheshgaya.android.basicnote.util

import android.content.Context
import com.maheshgaya.android.basicnote.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Mahesh Gaya on 8/12/17.
 */

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

fun Long.toDate(context: Context): String{
    val df = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
    val fullFormat = df.format(this)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)

    val today = Calendar.getInstance()
    val time: String
    if (today.get(Calendar.YEAR) == df.calendar.get(Calendar.YEAR)){
        if (today.get(Calendar.DAY_OF_YEAR) == df.calendar.get(Calendar.DAY_OF_YEAR)){
            time = context.getString(R.string.today) + " " + timeFormat
        } else if ((today.get(Calendar.DAY_OF_YEAR) - 1 == df.calendar.get(Calendar.DAY_OF_YEAR))){
            time = context.getString(R.string.yesterday) + " " + timeFormat
        } else {
            time = fullFormat
        }

    } else {
      time = fullFormat
    }

    return String.format(context.getString(R.string.last_edited) + " %s", time)
}


fun Date.formatDate(): String = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(this)

fun parseDate(month: Int, date: Int, year: Int): Date = SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(
        (month + 1).toString() + "/" + date.toString() + "/" + year.toString())