package com.maheshgaya.android.basicnote

import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Mahesh Gaya on 9/2/17.
 */
open class BaseActivitiy: AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        FirebaseDatabase.getInstance().goOnline()
    }

    override fun onStop() {
        super.onStop()
        FirebaseDatabase.getInstance().goOffline()
    }
}