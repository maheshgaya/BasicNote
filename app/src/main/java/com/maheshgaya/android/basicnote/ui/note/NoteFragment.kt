package com.maheshgaya.android.basicnote.ui.note

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spannable
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.widget.NoteEditorMenu


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteFragment: Fragment(), NoteEditorMenu.Callback {

    private lateinit var mToolbar: Toolbar
    private lateinit var mTitleEditText: EditText
    private lateinit var mBodyEditText: EditText
    private lateinit var mEditorMenu: NoteEditorMenu

    companion object {
        private val TAG = NoteFragment::class.simpleName
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note, container, false)
        mTitleEditText = rootView.findViewById(R.id.note_title_edittext)
        mBodyEditText = rootView.findViewById(R.id.note_body_edittext)
        mBodyEditText.requestFocus()
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mToolbar = activity.bind(R.id.toolbar)
        (activity as NoteActivity).setSupportActionBar(mToolbar)
        (activity as NoteActivity).supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        (activity as NoteActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as NoteActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        (activity as NoteActivity).supportActionBar!!.title = ""

        mEditorMenu = activity.findViewById(R.id.note_editor_menu)
        mEditorMenu.setCallback(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item!!.itemId) {
            R.id.action_share -> {

                val positionStart = mBodyEditText.selectionStart
                val positionEnd = mBodyEditText.selectionEnd
                mBodyEditText.text.setSpan(StyleSpan(Typeface.BOLD), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                val style = mBodyEditText.text.getSpans(positionStart, positionEnd, StyleSpan::class.java)

                if(style[0].style == Typeface.BOLD){
                    Toast.makeText(context, style[0].toString(), Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.d(TAG, Html.toHtml(mBodyEditText.text, 0).toString())
                    } else {
                        Log.d(TAG,Html.toHtml(mBodyEditText.text).toString())
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }

    fun saveToDatabase(note: Note){
    }

    override fun onBoldClick() {
        Toast.makeText(context, "onBoldClick", Toast.LENGTH_SHORT).show()
    }

    override fun onItalicClick() {
        Toast.makeText(context, "onItalicClick", Toast.LENGTH_SHORT).show()
    }

    override fun onUnderlineClick() {
        Toast.makeText(context, "onUnderlineClick", Toast.LENGTH_SHORT).show()
    }


}