package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.widget.SearchEditTextLayout


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





}