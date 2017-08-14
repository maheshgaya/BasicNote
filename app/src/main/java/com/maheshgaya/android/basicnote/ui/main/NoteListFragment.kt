package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteListFragment : Fragment(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id){

        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note_list, container, false)
        return rootView
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