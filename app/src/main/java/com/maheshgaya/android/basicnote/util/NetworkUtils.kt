package com.maheshgaya.android.basicnote.util

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import com.google.firebase.auth.FirebaseAuth


/**
 * Created by Mahesh Gaya on 8/14/17.
 */
fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

/**
 * Signs the user out
 */
fun signOut() {
    FirebaseAuth.getInstance().signOut()
}
