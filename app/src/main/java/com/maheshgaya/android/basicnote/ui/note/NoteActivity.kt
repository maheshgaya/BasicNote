package com.maheshgaya.android.basicnote.ui.note

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.maheshgaya.android.basicnote.BaseActivitiy
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.ui.main.MainActivity

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteActivity : BaseActivitiy() {
    companion object {
        private val FRAG_ID = "frag_note"
        private val TAG = NoteActivity::class.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val fragment = NoteFragment::class.java.newInstance()
        if (supportFragmentManager.findFragmentByTag(FRAG_ID) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.framelayout_note, fragment, FRAG_ID)
                    .commit()
        }
    }

}