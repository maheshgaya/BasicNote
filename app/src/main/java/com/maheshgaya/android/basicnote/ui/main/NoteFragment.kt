package com.maheshgaya.android.basicnote.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.ui.auth.AuthActivity
import com.maheshgaya.android.basicnote.util.startSearchActivity

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteFragment: Fragment(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.notes_textview -> {
                startActivity(Intent(activity, AuthActivity::class.java))
            }
        }
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note, container, false)
        rootView.findViewById<TextView>(R.id.notes_textview).setOnClickListener(this)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item!!.itemId) {
                R.id.action_search -> {
                    startSearchActivity(activity)
                    true
                }

                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
}