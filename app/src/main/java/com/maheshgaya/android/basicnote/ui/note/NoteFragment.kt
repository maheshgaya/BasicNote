package com.maheshgaya.android.basicnote.ui.note

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.util.toHtml
import com.maheshgaya.android.basicnote.widget.NoteEditText
import com.maheshgaya.android.basicnote.widget.NoteEditorMenu


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteFragment: Fragment(), NoteEditorMenu.Callback {

    private lateinit var mToolbar: Toolbar
    private lateinit var mTitleEditText: EditText
    private lateinit var mBodyEditText: NoteEditText
    private lateinit var mEditorMenu: NoteEditorMenu

    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance()
    private val mUser = mAuth.currentUser
    private var mKey = ""

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
                saveToDatabase()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }

    fun saveToDatabase(){
        val note = Note(mUser?.uid, toHtml(mTitleEditText.text),
                toHtml(mBodyEditText.text))
        if (mKey.isEmpty()) {
            mKey = mDatabase.getReference(Note.TABLE_NAME).push().key
        }
        mDatabase.getReference(Note.TABLE_NAME + "/" + mKey).setValue(note)
    }

    override fun onBoldClick() {
        val positionStart = mBodyEditText.selectionStart
        val positionEnd = mBodyEditText.selectionEnd
        val spanStyles = mBodyEditText.text.getSpans(positionStart, positionEnd, StyleSpan::class.java)
        if (spanStyles.isEmpty()){
            mBodyEditText.text.setSpan(StyleSpan(Typeface.BOLD), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getBoldCheckedButton().isChecked = true
        } else {
            for (charStyle in spanStyles) {
                if (charStyle.style == Typeface.BOLD) {
                    mBodyEditText.text.removeSpan(charStyle)
                }
                mEditorMenu.getBoldCheckedButton().isChecked = false
            }
        }
    }

    override fun onItalicClick() {
        val positionStart = mBodyEditText.selectionStart
        val positionEnd = mBodyEditText.selectionEnd
        val spanStyles = mBodyEditText.text.getSpans(positionStart, positionEnd, StyleSpan::class.java)
        if (spanStyles.isEmpty()){
            mBodyEditText.text.setSpan(StyleSpan(Typeface.ITALIC), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getItalicCheckedButton().isChecked = true
        } else {
            for (charStyle in spanStyles) {
                if (charStyle.style == Typeface.ITALIC) {
                    mBodyEditText.text.removeSpan(charStyle)
                }
                mEditorMenu.getItalicCheckedButton().isChecked = false
            }
        }
    }

    override fun onUnderlineClick() {
        val positionStart = mBodyEditText.selectionStart
        val positionEnd = mBodyEditText.selectionEnd
        val spanStyles = mBodyEditText.text.getSpans(positionStart, positionEnd, UnderlineSpan::class.java)
        Log.d(TAG, spanStyles.size.toString())
        if (spanStyles.isEmpty()){
            mBodyEditText.text.setSpan(UnderlineSpan(), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getUnderlineCheckedButton().isChecked = true
        } else {
            for (charStyle in spanStyles) {
                mBodyEditText.text.removeSpan(charStyle)
                mEditorMenu.getUnderlineCheckedButton().isChecked = false
            }
        }
    }


}