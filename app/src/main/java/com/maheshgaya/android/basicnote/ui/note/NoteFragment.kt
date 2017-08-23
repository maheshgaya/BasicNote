package com.maheshgaya.android.basicnote.ui.note

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
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
class NoteFragment : Fragment(), NoteEditText.HasEditedCallback {

    /** fragment toolbar */
    private lateinit var mToolbar: Toolbar
    /** title for note */
    private lateinit var mTitleEditText: EditText
    /** body for note */
    private lateinit var mBodyEditText: NoteEditText
    /** last edited textview */
    private lateinit var mLastEditedTextView: TextView

    private lateinit var mEditorMenu:NoteEditorMenu
    private lateinit var mCoordinatorLayout: CoordinatorLayout

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
    private var mMainNote: Boolean = true

    companion object {
        //for logging purposes
        private val TAG = NoteFragment::class.simpleName
        val NOTE_KEY = "note_key"
        val NOTE_MAIN = "note_main"
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
        mTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                mHasEdited = true
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                mHasEdited = true
            }

        })

        mBodyEditText.addCallback(this)
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
        mBodyEditText.addNoteMenu(mEditorMenu)
        mLastEditedTextView = activity.findViewById(R.id.last_edited_textview)
        mCoordinatorLayout = activity.findViewById(R.id.coordinator)

    }

    override fun onResume() {
        super.onResume()
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    override fun onStart() {
        super.onStart()
        if (mBodyEditText.text.isEmpty()) mBodyEditText.requestFocus()
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
        if (mHasEdited) { saveToDatabase() }
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
            mMainNote = savedInstanceState.getBoolean(NOTE_MAIN, true)
            mKey = if (!mNote.id.isNullOrEmpty()) mNote.id!! else ""
            updateUI()
        } else if (activity.intent != null && activity.intent.hasExtra(NOTE_KEY)) {
            //from another activity
            mNote = activity.intent.getParcelableExtra<Note>(NOTE_KEY)
            mMainNote = activity.intent.getBooleanExtra(NOTE_MAIN, true)
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
                R.id.action_share -> { true }
                R.id.action_save -> {
                    saveToDatabase()
                    true
                }
                android.R.id.home -> {
                    activity.supportFinishAfterTransition()
                    activity.onBackPressed()
                    true
                }
                else -> { super.onOptionsItemSelected(item) }
            }


    /**
     * Save to main notes sub tree
     */
    private fun saveToDatabase() {
        clearComposingText()
        if (mBodyEditText.text.isEmpty() && mTitleEditText.text.isEmpty() && mKey.isEmpty()) {
            mCoordinatorLayout.showSnackbar(getString(R.string.empty_note_not_saved))
            return
        }
        //notes/{uid}/main or notes/{uid}/trash
        val userRef = if (mMainNote) Note.getMainPath(mUser?.uid) else Note.getTrashPath(mUser?.uid)
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
    }

    private fun clearComposingText() {
        mBodyEditText.clearComposingText()
        mTitleEditText.clearComposingText()
    }

    override fun setEditedState(value:Boolean) {
        mHasEdited = value
    }


}