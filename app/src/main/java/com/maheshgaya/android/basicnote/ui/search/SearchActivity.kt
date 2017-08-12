package com.maheshgaya.android.basicnote.ui.search

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.bind

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SearchActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar: Toolbar = bind(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        supportFinishAfterTransition()
        super.onDestroy()
    }



}