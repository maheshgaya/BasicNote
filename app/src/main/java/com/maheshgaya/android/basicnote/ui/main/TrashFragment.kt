package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.startSearchActivity


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class TrashFragment: Fragment() {
    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_trash, container, false)

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