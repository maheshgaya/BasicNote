package com.maheshgaya.android.basicnote.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.StyleSpan
import android.util.Log
import android.widget.EditText

/**
 * Created by Mahesh Gaya on 8/18/17.
 */
fun EditText.removeSelectedTextSpan(spanStyle: StyleSpan, selStart: Int, selEnd: Int, styleInt: Int){
    val TAG = "EditText"
    val spanStart = text.getSpanStart(spanStyle)
    val spanEnd = text.getSpanEnd(spanStyle)
    Log.d(TAG, ":removeSelectedTextSpan::SpanStart= $spanStart\tSpanEnd= $spanEnd\tSelStart= $selStart\tSelEnd= $selEnd")

    //example: <b>Hello</b>
    if (selStart == spanStart && selEnd == spanEnd){
        Log.d(TAG, "first if")
        //<b>Hello</b>: selected=Hello
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else if (selStart > spanStart && selEnd < spanEnd){
        Log.d(TAG, "second if")
        //<b>Hello</b>: selected=ell
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), spanStart, selStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(StyleSpan(styleInt), selEnd, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else if (selStart == spanStart && selEnd < spanEnd){
        Log.d(TAG, "third if")
        //<b>Hello</b>: selected=Hell
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), selEnd, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else if (selStart > spanStart && selEnd == spanEnd){
        Log.d(TAG, "fourth if")
        //<b>Hello</b>: selected=ello
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), spanStart, selStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else if (selStart > spanStart && selEnd > spanEnd){
        Log.d(TAG, "fifth if")
        //<b>Hello</b>: selected=ello World
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), spanStart, selStart, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else if (selStart < spanStart && selEnd < spanEnd){
        Log.d(TAG, "sixth if")
        //<b>Hello</b>: selected=Hey Hello
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), selEnd, spanEnd,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else {
        Log.d(TAG, "seventh:last if")
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(Typeface.NORMAL), selStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}

fun EditText.setSelectedTextSpan(spanStyle: StyleSpan, selStart: Int, selEnd: Int, styleInt: Int){
    val TAG = "EditText"
    val spanStart = text.getSpanStart(spanStyle)
    val spanEnd = text.getSpanEnd(spanStyle)
    Log.d(TAG, ":setSelectedTextSpan::SpanStart= $spanStart\tSpanEnd= $spanEnd")

    //Example: Hey <b>Hello</b> World
    if (selStart >= spanStart && selEnd > spanEnd){
        Log.d(TAG, "first if set")
        //ello World or Hello World
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), spanStart, selEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else if (selStart < spanStart && selEnd <= spanEnd){
        Log.d(TAG, "second if set")
        //Hey Hell or Hey Hello
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), selStart, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    } else{
        Log.d(TAG, "third:last if set")
        //if (selStart < spanStart && selEnd > spanEnd)
        //Hey Hello World
        text.removeSpan(spanStyle)
        text.setSpan(StyleSpan(styleInt), selStart, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}