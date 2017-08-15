package com.maheshgaya.android.basicnote.ui.search

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.util.bind

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SearchResultFragment : Fragment(){
    private var mList = mutableListOf<Note>()

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_search_results, container, false)

        return rootView
    }



}