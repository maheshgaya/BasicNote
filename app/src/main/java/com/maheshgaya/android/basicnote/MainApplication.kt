package com.maheshgaya.android.basicnote

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class MainApplication:Application() {
    /**
     * Keep Firebase persistence for offline transaction
     */
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}