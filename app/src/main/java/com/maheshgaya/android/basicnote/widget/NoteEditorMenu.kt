package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/13/17.
 */
class NoteEditorMenu(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {
    private var mMenuLayout: View
    private var mBoldCheckedButton: CheckedButton
    private var mItalicCheckedButton: CheckedButton
    private var mUnderlineCheckedButton: CheckedButton

    interface Callback{
        fun onBoldClick()
        fun onItalicClick()
        fun onUnderlineClick()
    }

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

    fun setCallback(listener: Callback) {
        mCallback = listener
    }


    private var mCallback:Callback? = null

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.format_bold_imagebutton -> {
                mBoldCheckedButton.toggle()
                mCallback!!.onBoldClick()
            }
            R.id.format_italic_imagebutton -> {
                mItalicCheckedButton.toggle()
                mCallback!!.onItalicClick()
            }
            R.id.format_underline_imagebutton -> {
                mUnderlineCheckedButton.toggle()
                mCallback!!.onUnderlineClick()
            }
        }
    }
}