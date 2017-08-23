package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.maheshgaya.android.basicnote.R
import android.view.ViewGroup


/**
 * Created by Mahesh Gaya on 8/13/17.
 * This is the editor menu for creating and editing notes
 * It handles bold, italic, and underline
 */
class NoteEditorMenu(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {
    /** The outer layout */
    private var mMenuLayout: View
    /** The bold checked button */
    var boldCheckedButton: CheckedButton
    /** The italic checked button */
    var italicCheckedButton: CheckedButton
    /** The underline checked button */
    var underlineCheckedButton: CheckedButton
    private var mMainLayout: FrameLayout

    /** Callback for bold, italic, and underline button */
    interface Callback {
        fun onBoldClick()
        fun onItalicClick()
        fun onUnderlineClick()
    }

    /**
     * This is the default constructor
     * Inflate and initialize the views
     * Add click listener to the buttons
     */
    init {
        View.inflate(context, R.layout.layout_note_editor_menu, this)
        mMenuLayout = findViewById(R.id.note_editor_menu_layout)
        mMainLayout = findViewById(R.id.note_editor_menu_framelayout)
        boldCheckedButton = findViewById(R.id.format_bold_imagebutton)
        italicCheckedButton = findViewById(R.id.format_italic_imagebutton)
        underlineCheckedButton = findViewById(R.id.format_underline_imagebutton)

        boldCheckedButton.setOnClickListener(this)
        italicCheckedButton.setOnClickListener(this)
        underlineCheckedButton.setOnClickListener(this)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM
        mMainLayout.layoutParams = params
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