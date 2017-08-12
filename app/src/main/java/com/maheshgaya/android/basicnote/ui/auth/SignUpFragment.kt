package com.maheshgaya.android.basicnote.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.User
import com.maheshgaya.android.basicnote.ui.main.MainActivity
import com.maheshgaya.android.basicnote.util.bind

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SignUpFragment: Fragment(), View.OnClickListener, IAuth.SignUp {
    companion object {
        private val TAG = SignUpFragment::class.simpleName
    }
    private lateinit var mEmailSignUpButton: Button
    private lateinit var mGoogleSignUpButton: SignInButton
    private lateinit var mSignInTextView: TextView

    private lateinit var mEmailTextInputLayout: TextInputLayout
    private lateinit var mEmailTextEditText: TextInputEditText
    private lateinit var mPasswordTextInputLayout: TextInputLayout
    private lateinit var mPasswordTextEditText: TextInputEditText
    private lateinit var mFirstNameTextInputLayout: TextInputLayout
    private lateinit var mFirstNameTextEditText: TextInputEditText
    private lateinit var mLastNameTextInputLayout: TextInputLayout
    private lateinit var mLastNameTextEditText: TextInputEditText

    private val mFirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_sign_up, container, false)
        mEmailSignUpButton = rootView.bind(R.id.sign_up_with_email_button)
        mGoogleSignUpButton = rootView.bind(R.id.sign_up_with_google_button)
        mSignInTextView = rootView.bind(R.id.sign_in_has_account_textview)

        mEmailTextInputLayout = rootView.bind(R.id.auth_email_textinputlayout)
        mEmailTextEditText = rootView.bind(R.id.auth_email_textedittext)
        mPasswordTextInputLayout = rootView.bind(R.id.auth_password_textinputlayout)
        mPasswordTextEditText = rootView.bind(R.id.auth_password_textedittext)
        mFirstNameTextInputLayout = rootView.bind(R.id.auth_firstname_textinputlayout)
        mFirstNameTextEditText = rootView.bind(R.id.auth_firstname_textedittext)
        mLastNameTextInputLayout = rootView.bind(R.id.auth_lastname_textinputlayout)
        mLastNameTextEditText = rootView.bind(R.id.auth_lastname_textedittext)

        mSignInTextView.setOnClickListener(this)
        //configure google sign up button
        (0..mGoogleSignUpButton.childCount)
                .map { mGoogleSignUpButton.getChildAt(it) }
                .filterIsInstance<TextView>()
                .forEach { it.text = getString(R.string.sign_up_with_google) }
        return rootView
    }

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.sign_in_has_account_textview -> {
                val fragment = AuthActivity.mFragmentList[R.layout.fragment_sign_in]!!.newInstance() as Fragment
                fragmentManager.beginTransaction().replace(R.id.framelayout_auth, fragment, AuthActivity.FRAG_ID).commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = mFirebaseAuth.currentUser
        updateUI(user)
    }

    override fun createAccount(email: String, password: String) {
        if (!validateInput()) return
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mFirebaseAuth.currentUser
                        updateUI(user)
                        // create user record
                        val database = FirebaseDatabase.getInstance()
                        val ref = database.getReference("users/" + user!!.uid)
                        //save the user's record to database
                        ref.setValue(User(user.uid, mFirstNameTextEditText.text.toString(),
                                mLastNameTextEditText.text.toString(), user.email))
                        Toast.makeText(context, getString(R.string.account_successfully_created), Toast.LENGTH_SHORT).show()

                        mFirebaseAuth.currentUser!!.sendEmailVerification()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(context, getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }


                }
    }

    override fun createAccount(acct: GoogleSignInAccount) {
        if (!validateInput()) return
        TODO("Implement with Google Sign In")
    }

    override fun updateUI(user: FirebaseUser?) {
       if (user != null){
           startActivity(Intent(activity, MainActivity::class.java))
       }
    }

    override fun validateInput(): Boolean {
        val required = getString(R.string.required)
        var valid = true
        //TODO("validate email and password")
        mEmailTextInputLayout.error = if (mEmailTextEditText.text.isNullOrBlank()){
            valid = false
            required
        } else { null }

        mPasswordTextInputLayout.error = if (mPasswordTextEditText.text.isNullOrBlank()){
            valid = false
            required
        } else { null }

        mFirstNameTextInputLayout.error = if (mFirstNameTextEditText.text.isNullOrBlank()){
            valid = false
            required
        } else { null }

        mLastNameTextInputLayout.error = if (mLastNameTextEditText.text.isNullOrBlank()){
            valid = false
            required
        } else { null }
        return valid
    }

}