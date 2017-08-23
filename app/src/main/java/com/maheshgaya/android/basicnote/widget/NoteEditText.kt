package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.toHtml

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
        val html = text.toHtml()
        Log.d(TAG, "onSelectionChanged=\n$html\nselStart=$selStart\tselEnd=$selEnd" )
    }

    private fun handleUnderlineStyle(selStart: Int, selEnd: Int){

    }

    private fun handleSpannableStyle(selStart: Int, selEnd: Int, type:Int){

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
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
}