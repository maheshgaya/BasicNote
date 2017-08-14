package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import com.maheshgaya.android.basicnote.R
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maheshgaya.android.basicnote.adapter.NoteListAdapter
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.util.bind


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteListFragment : Fragment(), View.OnClickListener {

    companion object {
        private val TAG = NoteListFragment::class.simpleName
    }

    private var mNoteList: MutableList<Note> = mutableListOf()
    private var mNoteAdapter = NoteListAdapter(mNoteList)
    //Views
    private lateinit var mNoteRecyclerView: RecyclerView

    //Firebase
    private val mUser = FirebaseAuth.getInstance().currentUser
    private val mDatabaseRef = FirebaseDatabase.getInstance()
            .getReference(Note.TABLE_NAME + "/" + mUser?.uid)

    var mNoteValueListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.childrenCount > 0) mNoteList.clear()
            for (snapshot in dataSnapshot.children){
                val note = snapshot.getValue<Note>(Note::class.java)
                mNoteList.add(note!!)
                mNoteList.reverse()
                mNoteAdapter.notifyDataSetChanged()
                Log.d(TAG, note.toString())
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDatabaseRef.addValueEventListener(mNoteValueListener)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note_list, container, false)
        mNoteRecyclerView = rootView.bind(R.id.note_recyclerview)
        mNoteRecyclerView.adapter = mNoteAdapter
        mNoteRecyclerView.layoutManager = LinearLayoutManager(context)
        return rootView
    }
    override fun onClick(view: View?) {
        when (view!!.id){

        }
    }

    inner class Callback() : ActionMode.Callback{
        override fun onActionItemClicked(mode: ActionMode?, menuItem: MenuItem?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCreateActionMode(mode: ActionMode?, menuItem: Menu?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPrepareActionMode(mode: ActionMode?, menuItem: Menu?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}