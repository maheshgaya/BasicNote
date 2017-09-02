package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.toHtml
import java.lang.reflect.Type

/**
 * Created by Mahesh Gaya on 8/14/17.
 */
class NoteEditText : EditText, NoteEditorMenu.Callback {

    companion object {
        private val TAG = NoteEditText::class.simpleName
    }

    interface HasEditedCallback {
        fun setEditedState(value:Boolean)
    }

    private var mCallback: HasEditedCallback? = null
    private var mNoteMenu: NoteEditorMenu? = null

    fun addNoteMenu(noteMenu: NoteEditorMenu){
        mNoteMenu = noteMenu
        mNoteMenu?.setCallback(this)
    }

    fun addCallback(callback: HasEditedCallback){
        mCallback = callback
    }
    /**
     * Invoke constructor
     */
    constructor(context: Context?) : this(context, null)

    /**
     * Invoke constructor
     */
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, R.attr.noteEditText)

    /**
     * Invoke constructor
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    /**
     * initialize the attributes
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        //get list of attributes
        val a = context!!.obtainStyledAttributes(
                attrs, R.styleable.NoteEditText, defStyleAttr, 0
        )
        //TODO attributes, check res/values/attrs.xml for NoteEditText attributes

        a.recycle()
        addTextChangedListener(NoteTextWatcher())
    }



    /**
     * Listens for selected text and sets callback
     * @param selStart Beginning of the selected text
     * @param selEnd Ending of the selected text
     */
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        Log.d(TAG, "onSelectionChanged: selStart=$selStart\tselEnd=$selEnd")
        var bold = false
        var italic = false
        var underline = false

        val spanArray = when {
            selStart > 0 && selStart == selEnd -> text.getSpans(selStart - 1, selEnd, CharacterStyle::class.java)
            text.isNotEmpty() -> text.getSpans(selStart, selEnd, CharacterStyle::class.java)
            else -> emptyArray()
        }

        spanArray
                .forEach {
                    when (it) {
                        is UnderlineSpan -> underline = true
                        is StyleSpan -> when {
                            it.style == Typeface.BOLD -> bold = true
                            it.style == Typeface.ITALIC -> italic = true
                            it.style == Typeface.BOLD_ITALIC -> {
                                italic = true
                                bold = true
                            }
                        }
                    }
                }
        
        mNoteMenu?.boldCheckedButton?.isChecked = bold
        mNoteMenu?.italicCheckedButton?.isChecked = italic
        mNoteMenu?.underlineCheckedButton?.isChecked = underline

    }

    private fun handleUnderlineStyle(selStart: Int, selEnd: Int){
        var exist = false
        val styleSpans = text.getSpans(selStart, selEnd, UnderlineSpan::class.java)
        var styleStart = -1
        var styleEnd = -1
        for (span in styleSpans){
            if (span is UnderlineSpan){
                if (text.getSpanStart(span) < selStart){
                    styleStart = text.getSpanStart(span)
                }
                if (text.getSpanEnd(span) > selEnd){
                    styleEnd = text.getSpanEnd(span)
                }
                text.removeSpan(span)
                exist = true
            }
        }

        if (styleStart > -1){
            text.setSpan(UnderlineSpan(), styleStart, selStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (styleEnd> -1){
            text.setSpan(UnderlineSpan(), selEnd, styleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (!exist){
            text.setSpan(UnderlineSpan(), selStart, selEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        } else {
            mNoteMenu?.underlineCheckedButton?.isChecked = false
        }
        this.setSelection(selStart, selEnd)

    }

    private fun handleSpannableStyle(selStart: Int, selEnd: Int, type:Int){
        var exist = false
        val styleSpans = text.getSpans(selStart, selEnd, StyleSpan::class.java)
        var styleStart = -1
        var styleEnd = -1
        for (span in styleSpans){
           if (span.style == type){
               if (text.getSpanStart(span) < selStart){
                   styleStart = text.getSpanStart(span)
               }
               if (text.getSpanEnd(span) > selEnd){
                   styleEnd = text.getSpanEnd(span)
               }
               text.removeSpan(span)
               exist = true
           }
        }

        if (styleStart > -1){
            text.setSpan(StyleSpan(type), styleStart, selStart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (styleEnd> -1){
            text.setSpan(StyleSpan(type), selEnd, styleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (!exist){
            text.setSpan(StyleSpan(type), selStart, selEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        } else {
            when (type){
                Typeface.BOLD -> {
                    mNoteMenu?.boldCheckedButton?.isChecked = false
                }
                Typeface.ITALIC -> {
                    mNoteMenu?.italicCheckedButton?.isChecked = false
                }
            }
        }
        this.setSelection(selStart, selEnd)



    }

    fun styleBold(){
        handleSpannableStyle(selectionStart, selectionEnd, Typeface.BOLD)
    }


    fun styleItalic() {
        handleSpannableStyle(selectionStart, selectionEnd, Typeface.ITALIC)
    }

    fun styleUnderline() {
        handleUnderlineStyle(selectionStart, selectionEnd)
    }



    override fun onBoldClick() {
        mCallback?.setEditedState(true)
        styleBold()
    }

    override fun onItalicClick() {
        mCallback?.setEditedState(true)
        styleItalic()
    }

    override fun onUnderlineClick() {
        mCallback?.setEditedState(true)
        styleUnderline()
    }

    inner class NoteTextWatcher:TextWatcher{
        override fun afterTextChanged(editable: Editable?) {
            clearComposingText()
            Log.d(TAG, "afterTextChanged= " + editable?.toHtml())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            clearComposingText()
            Log.d(TAG, "beforeTextChanged= $s"  )
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearComposingText()
            Log.d(TAG, "onTextChanged= $s")
        }

    }
}