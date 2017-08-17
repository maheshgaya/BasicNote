package com.maheshgaya.android.basicnote.ui.profile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.ui.note.NoteActivity
import com.maheshgaya.android.basicnote.util.bind

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class ProfileActivity: AppCompatActivity() {
    private lateinit var mToolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mToolbar = bind(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }
}