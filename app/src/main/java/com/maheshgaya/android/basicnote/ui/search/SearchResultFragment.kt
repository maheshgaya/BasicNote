package com.maheshgaya.android.basicnote.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.adapter.NoteListAdapter
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.widget.EmptyView


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SearchResultFragment : Fragment() {

    private var mNoteList = mutableListOf<Note>()
    private var mNoteAdapter = NoteListAdapter(null, mNoteList)
    private lateinit var mNoteRecyclerView: RecyclerView
    private lateinit var mEmptyView:EmptyView
    lateinit var mMainView:View

    var searchQuery = ""
        set(value) {
            field = value
            mNoteList.clear()
            mNoteAdapter.notifyDataSetChanged()
            handleQuery()
            Log.d(TAG, searchQuery)
        }

    var mainSearch = true

    private val mUser = FirebaseAuth.getInstance().currentUser

    companion object {
        val TAG = SearchResultFragment::class.simpleName
    }

    init {
        setHasOptionsMenu(true)
    }

    private fun handleQuery() {
        val databaseRef = FirebaseDatabase.getInstance()
                .getReference( if (!mainSearch) Note.getTrashPath(mUser?.uid) else Note.getMainPath(mUser?.uid))
        val notesQuery = databaseRef.orderByChild(Note.COLUMN_BODY).endAt(searchQuery)
        mNoteList.clear()
        if (searchQuery.isEmpty()) setView()
        notesQuery.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(databaseError: DatabaseError?) { setView() }
            override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) { setView() }
            override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) { setView() }
            override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                if (dataSnapshot!!.childrenCount == 0.toLong()) return
                val note = dataSnapshot.getValue(Note::class.java)
                if (note!!.body.toLowerCase().contains(Regex(searchQuery.toLowerCase())) ||
                        note.title.toLowerCase().contains(Regex(searchQuery.toLowerCase()))){
                    Log.d(TAG, note.body + "\t" + note.title)
                    mNoteList.add(note)
                    mNoteAdapter.notifyDataSetChanged()
                    setView()
                }
            }
            override fun onChildRemoved(databaseError: DataSnapshot?) { setView() }
        })
    }

    fun clearList(){
        mNoteList.clear()
    }

    fun setView(){
        try {
            if (mNoteList.size == 0) {
                mEmptyView.visibility = View.VISIBLE; mMainView.visibility = View.GONE
            } else {
                mEmptyView.visibility = View.GONE; mMainView.visibility = View.VISIBLE
            }
        } catch (e: UninitializedPropertyAccessException){
            Log.e(TAG, e.stackTrace.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_search_results, container, false)
        mMainView = rootView.bind(R.id.search_results_view)
        mEmptyView = rootView.bind(R.id.empty_view)
        mNoteRecyclerView = rootView.bind(R.id.search_results_recyclerview)
        mNoteAdapter = NoteListAdapter(context, mNoteList)
        mNoteRecyclerView.adapter = mNoteAdapter
        mNoteRecyclerView.layoutManager = LinearLayoutManager(context)
        return rootView
    }

}