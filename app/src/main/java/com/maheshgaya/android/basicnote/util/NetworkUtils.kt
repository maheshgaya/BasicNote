package com.maheshgaya.android.basicnote.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.firebase.auth.FirebaseAuth


/**
 * Created by Mahesh Gaya on 8/14/17.
 */
fun Activity.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

/**
 * Signs the user out
 */
fun signOut() {
    FirebaseAuth.getInstance().signOut()
}
