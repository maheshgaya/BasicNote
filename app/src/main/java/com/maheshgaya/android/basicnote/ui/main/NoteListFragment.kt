package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
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
}