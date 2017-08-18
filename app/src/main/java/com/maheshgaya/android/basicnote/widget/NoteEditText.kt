package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/14/17.
 */
class NoteEditText : EditText {
    companion object {
        private val TAG = NoteEditText::class.simpleName
    }

    /**
     * Listeners for NoteEditText
     */
    interface NoteEditTextListener{
        /**
         * Listens for selected text
         * @param selStart Beginning of the selected text
         * @param selEnd Ending of the selected text
         */
        fun onSelectionChangedListener(selStart: Int, selEnd: Int)
    }

    private var mListener: NoteEditTextListener? = null

    /**
     * Adds listener to the EditText
     * @param listener Add listener to NoteEditText
     */
    fun addListener(listener: NoteEditTextListener){
        mListener = listener
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
    }


    /**
     * Listens for selected text and sets callback
     * @param selStart Beginning of the selected text
     * @param selEnd Ending of the selected text
     */
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        mListener?.onSelectionChangedListener(selStart, selEnd)
    }
}