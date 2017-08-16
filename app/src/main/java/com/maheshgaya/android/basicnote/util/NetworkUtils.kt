package com.maheshgaya.android.basicnote.util

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.firebase.auth.FirebaseAuth


/**
 * Created by Mahesh Gaya on 8/14/17.
 */
fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo:NetworkInfo = connectivityManager.activeNetworkInfo
    return networkInfo.isConnectedOrConnecting
}

/**
 * Signs the user out
 */
fun signOut() {
    FirebaseAuth.getInstance().signOut()
}
