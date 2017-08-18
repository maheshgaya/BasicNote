package com.maheshgaya.android.basicnote.ui.note

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.util.*
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
    /** last edited textview */
    private lateinit var mLastEditedTextView: TextView

    private lateinit var mCoordinatorLayout:CoordinatorLayout

    //Firebase variables
    /** get instance of the FirebaseAuth */
    private val mAuth = FirebaseAuth.getInstance()
    /** get instance of the FirebaseDatabase */
    private val mDatabase = FirebaseDatabase.getInstance()
    /** get currentUser */
    private val mUser = mAuth.currentUser
    /** initialize key */
    private var mKey = ""
    /** keep the current note */
    private var mNote = Note()

    private var mHasEdited = false

    companion object {
        //for logging purposes
        private val TAG = NoteFragment::class.simpleName
        val NOTE_KEY = "note_key"
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
        mBodyEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                mHasEdited = true
                Log.d(TAG + ":afterTextChanged", mHasEdited.toString())
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG + ":beforeTextChanged", mHasEdited.toString())
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                mHasEdited = true
                Log.d(TAG + ":onTextChanged", mHasEdited.toString())
            }

        })
        mTitleEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                mHasEdited = true
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mHasEdited = true
            }

        })
        mHasEdited = false
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
        mLastEditedTextView = activity.findViewById(R.id.last_edited_textview)
        mCoordinatorLayout = activity.findViewById(R.id.coordinator)

    }

    override fun onStart() {
        super.onStart()
        if (mBodyEditText.text.isEmpty())  mBodyEditText.requestFocus()
    }

    /**
     * save to db if user quits the fragment or orientation changes
     */
    override fun onPause() {
        super.onPause()
        if (mHasEdited) {
            saveToDatabase()
        }
    }

    /**
     * saves state of this fragment
     * @param outState bundle to save
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        if (mHasEdited) {
            saveToDatabase()
        }
        outState?.putParcelable(NOTE_KEY, mNote)

        super.onSaveInstanceState(outState)

    }

    /**
     * Restore note
     * @param savedInstanceState bundle from another activity or this activity
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(NOTE_KEY)) {
            //for change in orientation
            mNote = savedInstanceState.getParcelable(NOTE_KEY)
            mKey = if (!mNote.id.isNullOrEmpty()) mNote.id!! else ""
            updateUI()
        } else if (activity.intent != null && activity.intent.hasExtra(NOTE_KEY)){
            //from another activity
            mNote = activity.intent.getParcelableExtra<Note>(NOTE_KEY)
            mKey = mNote.id!!
            updateUI()
        }

        super.onViewStateRestored(savedInstanceState)

    }

    private fun updateUI() {
        mTitleEditText.setText(mNote.title.fromHtml().trim())
        mBodyEditText.setText(mNote.body.fromHtml().trim())
        mLastEditedTextView.text = mNote.lastEdited.toLastedEditedDate(context)
        mHasEdited = false
        Log.d(TAG + ":updateUI", mHasEdited.toString())
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
                true
            }
            android.R.id.home -> {
                activity.supportFinishAfterTransition()
                activity.onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }



    /**
     * Save to main notes sub tree
     */
    private fun saveToDatabase(){
        clearComposingText()
        if (mBodyEditText.text.isEmpty() && mTitleEditText.text.isEmpty() && mKey.isEmpty()) {
            mCoordinatorLayout.showSnackbar(getString(R.string.empty_note_not_saved))
            return
        }
        //notes/{uid}/main
        val userRef = Note.getMainPath(mUser?.uid)
        //if key is empty, get a new key
        if (mKey.isEmpty()) {
            mKey = mDatabase.getReference(userRef).push().key
        }

        //if title is empty then untitled
        val title = if (mTitleEditText.text.isEmpty()) getString(R.string.untitled) else mTitleEditText.text.toHtml()
        //create a new note object
        mNote = Note(id = mKey, uid = mUser?.uid, title = title,
                body = mBodyEditText.text.toHtml())
        //append to user ref and create a new note to the database
        mDatabase.getReference(userRef + "/" + mKey).setValue(mNote)
        mLastEditedTextView.text = mNote.lastEdited.toLastedEditedDate(context)
        mHasEdited = false
        Log.d(TAG, "saveToDatabase()")
    }

    private fun clearComposingText(){
        mBodyEditText.clearComposingText()
        mTitleEditText.clearComposingText()
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
            spanStyles
                    .filter { it.style == Typeface.BOLD }
                    .forEach { mBodyEditText.text.removeSpan(it) }
            mEditorMenu.getBoldCheckedButton().isChecked = false
        }
        mHasEdited = true
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
            spanStyles
                    .filter { it.style == Typeface.ITALIC }
                    .forEach { mBodyEditText.text.removeSpan(it) }
            mEditorMenu.getItalicCheckedButton().isChecked = false
        }
        mHasEdited = true
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
        if (spanStyles.isEmpty()){
            //set underline
            mBodyEditText.text.setSpan(UnderlineSpan(), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mEditorMenu.getUnderlineCheckedButton().isChecked = true
        } else {
            //remove underline
            for (charStyle in spanStyles) {
                mBodyEditText.text.removeSpan(charStyle)
            }
            mEditorMenu.getUnderlineCheckedButton().isChecked = false
        }
        mHasEdited = true
    }

}