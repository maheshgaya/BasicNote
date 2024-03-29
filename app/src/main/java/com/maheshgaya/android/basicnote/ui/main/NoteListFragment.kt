package com.maheshgaya.android.basicnote.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.adapter.NoteListAdapter
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.ui.note.NoteActivity
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.widget.EmptyView


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteListFragment : Fragment(), EmptyView.Callback {

    companion object {
        private val TAG = NoteListFragment::class.simpleName
    }

    private var mNoteList: MutableList<Note> = mutableListOf()
    private var mNoteAdapter = NoteListAdapter(null, mNoteList)
    //Views
    private lateinit var mNoteRecyclerView: RecyclerView
    private lateinit var mEmptyView: EmptyView
    private lateinit var mMainView: View

    //Firebase
    private val mUser = FirebaseAuth.getInstance().currentUser
    private val mDatabaseRef = FirebaseDatabase.getInstance()
            .getReference(Note.getMainPath(mUser?.uid)).orderByKey()
    init {
        retainInstance = true
        mDatabaseRef.keepSynced(true)
    }

    private val mNoteValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.childrenCount > 0) mNoteList.clear()
            for (snapshot in dataSnapshot.children){
                val note = snapshot.getValue<Note>(Note::class.java)
                mNoteList.add(note!!)
                mNoteList.sortByDescending { it.lastEdited }
                mNoteAdapter.notifyDataSetChanged()
                setView()
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            setView()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDatabaseRef.addValueEventListener(mNoteValueListener)
    }

    override fun onDestroy() {
        mDatabaseRef?.removeEventListener(mNoteValueListener)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note_list, container, false)
        mNoteRecyclerView = rootView.bind(R.id.note_recyclerview)
        mNoteAdapter = NoteListAdapter(context, mNoteList)
        mNoteRecyclerView.adapter = mNoteAdapter
        mNoteRecyclerView.layoutManager = LinearLayoutManager(context)
        mMainView = rootView.bind(R.id.main_view)
        mEmptyView = rootView.bind(R.id.empty_view)
        mEmptyView.setCallback(this)
        return rootView
    }

    fun setView(){
        if (mNoteList.size == 0){ mEmptyView.visibility = View.VISIBLE; mMainView.visibility = View.GONE }
        else { mEmptyView.visibility = View.GONE; mMainView.visibility = View.VISIBLE }
    }

    override fun onEmptyButtonClick() {
        startActivity(Intent(activity, NoteActivity::class.java))
    }
}