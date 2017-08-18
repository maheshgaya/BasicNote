package com.maheshgaya.android.basicnote.ui.auth

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.util.isOnline
import com.maheshgaya.android.basicnote.util.showSnackbar


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SignInFragment: Fragment(), View.OnClickListener, IAuth.SignIn {
    companion object {
        private val TAG = SignInFragment::class.simpleName
    }

    private lateinit var mEmailSignInButton: Button
    private lateinit var mGoogleSignInButton: SignInButton
    private lateinit var mSignUpTextView: TextView

    private lateinit var mEmailTextInputLayout: TextInputLayout
    private lateinit var mEmailTextEditText: TextInputEditText
    private lateinit var mPasswordTextInputLayout: TextInputLayout
    private lateinit var mPasswordTextEditText: TextInputEditText
    private lateinit var mCoordinatorLayout: CoordinatorLayout

    private val mFirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_sign_in, container, false)
        mEmailSignInButton = rootView.bind(R.id.sign_in_with_email_button)
        mGoogleSignInButton = rootView.bind(R.id.sign_in_with_google_button)
        mSignUpTextView = rootView.bind(R.id.sign_up_no_account_textview)


        mEmailTextInputLayout = rootView.bind(R.id.auth_email_textinputlayout)
        mEmailTextEditText = rootView.bind(R.id.auth_email_textedittext)
        mPasswordTextInputLayout = rootView.bind(R.id.auth_password_textinputlayout)
        mPasswordTextEditText = rootView.bind(R.id.auth_password_textedittext)

        //configure google sign in button
        (0..mGoogleSignInButton.childCount)
                .map { mGoogleSignInButton.getChildAt(it) }
                .filterIsInstance<TextView>()
                .forEach { it.text = getString(R.string.sign_in_with_google) }

        mSignUpTextView.setOnClickListener(this)
        mEmailSignInButton.setOnClickListener(this)
        mGoogleSignInButton.setOnClickListener(this)
        return rootView
    }


    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.sign_up_no_account_textview -> {
                val fragment = AuthActivity.mFragmentList[R.layout.fragment_sign_up]!!.newInstance() as Fragment
                fragmentManager.beginTransaction().replace(R.id.framelayout_auth, fragment, AuthActivity.FRAG_ID).commit()
            }
            R.id.sign_in_with_email_button -> {
                signIn(mEmailTextEditText.text.toString(), mPasswordTextEditText.text.toString())
            }
            R.id.sign_in_with_google_button -> {
                signIn()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = mFirebaseAuth.currentUser
        updateUI(user)
    }

    override fun updateUI(user: FirebaseUser?) {
        if (user != null){
            (activity as AuthActivity).openMainActivity()
        }
    }

    override fun validateInput():Boolean {
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
        return valid
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCoordinatorLayout = activity.findViewById(R.id.coordinator)
        if (!activity.isOnline()) mCoordinatorLayout.showSnackbar(getString(R.string.offline))
    }

    override fun signIn(email: String, password: String) {
        if (!validateInput()) return
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity){ task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mFirebaseAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException())
                        mCoordinatorLayout.showSnackbar(getString(R.string.auth_failed))
                        updateUI(null)
                    }

                }
    }

    override fun signIn() {
        if (!validateInput()) return
        TODO("Implement with Google Sign In")
    }

}