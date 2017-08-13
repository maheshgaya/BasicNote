package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/13/17.
 */
class NoteEditorMenu(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {
    private var mMenuLayout: View
    private var mBoldImageView: View
    private var mItalicImageView: View
    private var mUnderlineImageView: View

    interface Callback{
        fun onBoldClick()
        fun onItalicClick()
        fun onUnderlineClick()
    }

    init {
        View.inflate(context, R.layout.layout_note_editor_menu, this)
        mMenuLayout = findViewById(R.id.note_editor_menu_layout)
        mBoldImageView = findViewById(R.id.format_bold_imagebutton)
        mItalicImageView = findViewById(R.id.format_italic_imagebutton)
        mUnderlineImageView = findViewById(R.id.format_underline_imagebutton)

        mBoldImageView.setOnClickListener(this)
        mItalicImageView.setOnClickListener(this)
        mUnderlineImageView.setOnClickListener(this)
    }

    fun setCallback(listener: Callback) {
        mCallback = listener
    }


    private var mCallback:Callback? = null

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.format_bold_imagebutton -> {
                Toast.makeText(context, "Bold", Toast.LENGTH_SHORT).show()
            }
            R.id.format_italic_imagebutton -> {
                Toast.makeText(context, "Italic", Toast.LENGTH_SHORT).show()
            }
            R.id.format_underline_imagebutton -> {
                Toast.makeText(context, "underline", Toast.LENGTH_SHORT).show()
            }
        }
    }
}