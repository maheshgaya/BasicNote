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

    /** fragment toolbar */
    private lateinit var mToolbar: Toolbar
    /** title for note */
    private lateinit var mTitleEditText: EditText
    /** body for note */
    private lateinit var mBodyEditText: NoteEditText
    /** editor menu */
    private lateinit var mEditorMenu: NoteEditorMenu

    //Firebase variables
    /** get instance of the FirebaseAuth */
    private val mAuth = FirebaseAuth.getInstance()
    /** get instance of the FirebaseDatabase */
    private val mDatabase = FirebaseDatabase.getInstance()
    /** get currentUser */
    private val mUser = mAuth.currentUser
    /** initialize key */
    private var mKey = ""

    companion object {
        //for logging purposes
        private val TAG = NoteFragment::class.simpleName
    }

    /**
     * set options menu to true
     */
    init {
        setHasOptionsMenu(true)
    }

    /**
     * initialize views for fragment
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note, container, false)
        mTitleEditText = rootView.findViewById(R.id.note_title_edittext)
        mBodyEditText = rootView.findViewById(R.id.note_body_edittext)
        mBodyEditText.requestFocus()
        return rootView
    }


    /**
     * initialize toolbar and editor menu
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //initialize toolbar and set title to empty
        mToolbar = activity.bind(R.id.toolbar)
        (activity as NoteActivity).setSupportActionBar(mToolbar)
        (activity as NoteActivity).supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        (activity as NoteActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as NoteActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        (activity as NoteActivity).supportActionBar!!.title = ""

        //find editor menu and set callback
        mEditorMenu = activity.findViewById(R.id.note_editor_menu)
        mEditorMenu.setCallback(this)
    }

    /**
     * Create menu
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.note_menu, menu)
    }

    /**
     * Handles menu actions
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item!!.itemId) {
            R.id.action_share -> {
                saveToDatabase() //TODO have autosave
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }

    /**
     * Save to main notes sub tree
     */
    fun saveToDatabase(){
        //notes/{uid}/main
        val userRef = Note.TABLE_NAME + "/" + mUser?.uid + "/" + Note.SUB_TABLE_MAIN
        //if key is empty, get a new key
        if (mKey.isEmpty()) {
            mKey = mDatabase.getReference(userRef).push().key
        }

        //if title is empty then untitled
        val title = if (mTitleEditText.text.isEmpty()) getString(R.string.untitled) else toHtml(mTitleEditText.text)
        //create a new note object
        val note = Note(id = mKey, uid = mUser?.uid, title = title,
                body = toHtml(mBodyEditText.text))
        //append to user ref and create a new note to the database
        mDatabase.getReference(userRef + "/" + mKey).setValue(note)
    }

    /**
     * Handles Bold button clicks
     */
    override fun onBoldClick() {
        //get start and end position
        val positionStart = mBodyEditText.selectionStart
        val positionEnd = mBodyEditText.selectionEnd
        //get styles from spans
        val spanStyles = mBodyEditText.text.getSpans(positionStart, positionEnd, StyleSpan::class.java)
        if (spanStyles.isEmpty()){
            //set bold
            mBodyEditText.text.setSpan(StyleSpan(Typeface.BOLD), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getBoldCheckedButton().isChecked = true
        } else {
            //remove bold
            for (charStyle in spanStyles) {
                if (charStyle.style == Typeface.BOLD) {
                    mBodyEditText.text.removeSpan(charStyle)
                }
                mEditorMenu.getBoldCheckedButton().isChecked = false
            }
        }
    }

    /**
     * Handles Italic button clicks
     */
    override fun onItalicClick() {
        //get start and end position
        val positionStart = mBodyEditText.selectionStart
        val positionEnd = mBodyEditText.selectionEnd
        //get styles from spans
        val spanStyles = mBodyEditText.text.getSpans(positionStart, positionEnd, StyleSpan::class.java)
        if (spanStyles.isEmpty()){
            //set italic
            mBodyEditText.text.setSpan(StyleSpan(Typeface.ITALIC), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getItalicCheckedButton().isChecked = true
        } else {
            //remove italic
            for (charStyle in spanStyles) {
                if (charStyle.style == Typeface.ITALIC) {
                    mBodyEditText.text.removeSpan(charStyle)
                }
                mEditorMenu.getItalicCheckedButton().isChecked = false
            }
        }
    }

    /**
     * Handles Underline button clicks
     */
    override fun onUnderlineClick() {
        //get start and end position
        val positionStart = mBodyEditText.selectionStart
        val positionEnd = mBodyEditText.selectionEnd
        //get styles from spans
        val spanStyles = mBodyEditText.text.getSpans(positionStart, positionEnd, UnderlineSpan::class.java)
        Log.d(TAG, spanStyles.size.toString())
        if (spanStyles.isEmpty()){
            //set underline
            mBodyEditText.text.setSpan(UnderlineSpan(), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getUnderlineCheckedButton().isChecked = true
        } else {
            //remove underline
            for (charStyle in spanStyles) {
                mBodyEditText.text.removeSpan(charStyle)
                mEditorMenu.getUnderlineCheckedButton().isChecked = false
            }
        }
    }

}