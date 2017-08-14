package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/13/17.
 * This is the editor menu for creating and editing notes
 * It handles bold, italic, and underline
 */
class NoteEditorMenu(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {
    /** The outer layout */
    private var mMenuLayout: View
    /** The bold checked button */
    private var mBoldCheckedButton: CheckedButton
    /** The italic checked button */
    private var mItalicCheckedButton: CheckedButton
    /** The underline checked button */
    private var mUnderlineCheckedButton: CheckedButton

    /** Callback for bold, italic, and underline button */
    interface Callback {
        fun onBoldClick()
        fun onItalicClick()
        fun onUnderlineClick()
    }

    /**
     * @return Instance of bold button
     */
    fun getBoldCheckedButton(): CheckedButton = mBoldCheckedButton

    /**
     * @return Instance of Italic button
     */
    fun getItalicCheckedButton(): CheckedButton = mItalicCheckedButton

    /**
     * @return Instance of Underline button
     */
    fun getUnderlineCheckedButton(): CheckedButton = mUnderlineCheckedButton

    /**
     * This is the default constructor
     * Inflate and initialize the views
     * Add click listener to the buttons
     */
    init {
        View.inflate(context, R.layout.layout_note_editor_menu, this)
        mMenuLayout = findViewById(R.id.note_editor_menu_layout)
        mBoldCheckedButton = findViewById(R.id.format_bold_imagebutton)
        mItalicCheckedButton = findViewById(R.id.format_italic_imagebutton)
        mUnderlineCheckedButton = findViewById(R.id.format_underline_imagebutton)

        mBoldCheckedButton.setOnClickListener(this)
        mItalicCheckedButton.setOnClickListener(this)
        mUnderlineCheckedButton.setOnClickListener(this)
    }

    /** Interface listener */
    private var mCallback: Callback? = null

    /**
     * @param listener NoteEditorMenu.Callback listener
     * Sets the member listener
     */
    fun setCallback(listener: Callback) {
        mCallback = listener
    }

    /**
     * @param view View from the UI
     * Handles onClick events
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.format_bold_imagebutton -> {
                //Adds callback for the bold button
                mCallback!!.onBoldClick()
            }
            R.id.format_italic_imagebutton -> {
                //Adds callback for the italic button
                mCallback!!.onItalicClick()
            }
            R.id.format_underline_imagebutton -> {
                //Adds callback for the underline button
                mCallback!!.onUnderlineClick()
            }
        }
    }
}