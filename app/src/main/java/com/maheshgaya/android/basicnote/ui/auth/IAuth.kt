package com.maheshgaya.android.basicnote.ui.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
interface IAuth {
    interface SignUp {
        fun createAccount(email: String, password: String)
        fun createAccount(acct: GoogleSignInAccount)
        fun updateUI(user: FirebaseUser?)
        fun validateInput(): Boolean
    }
    interface SignIn {
        fun signIn(email: String , password: String)
        fun signIn()
        fun updateUI(user: FirebaseUser?)
        fun validateInput(): Boolean
    }
}