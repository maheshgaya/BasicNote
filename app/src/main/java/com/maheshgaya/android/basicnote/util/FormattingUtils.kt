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
            .filterNot { stringArray[it].isEmpty() }
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

fun Long.toLastedEditedDate(context: Context): String = String.format(context.getString(R.string.last_edited) + " %s", this.time(context))

private fun Long.time(context: Context): String {
    val df = SimpleDateFormat("EEE, MMM d, yyyy hh:mma", Locale.getDefault())
    val fullFormat = df.format(this)
    val timeFormat = SimpleDateFormat(" hh:mma", Locale.getDefault()).format(this)
    val dayFormat = SimpleDateFormat("EEE hh:mma", Locale.getDefault()).format(this)

    val today = Calendar.getInstance()
    val time: String
    time = when {
        today.get(Calendar.DAY_OF_YEAR) == df.calendar.get(Calendar.DAY_OF_YEAR) ->
            context.getString(R.string.today) + timeFormat
        today.get(Calendar.DAY_OF_YEAR) - 1 == df.calendar.get(Calendar.DAY_OF_YEAR) ->
            context.getString(R.string.yesterday) + timeFormat
        today.get(Calendar.DAY_OF_YEAR) - 7 <= df.calendar.get(Calendar.DAY_OF_YEAR) ->
            dayFormat
        else -> fullFormat
    }


    return time
}

fun Long.toDate(context: Context): String = String.format("%s", this.time(context))


fun Date.formatDate(): String = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(this)

fun parseDate(month: Int, date: Int, year: Int): Date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(
        (month + 1).toString() + "/" + date.toString() + "/" + year.toString())