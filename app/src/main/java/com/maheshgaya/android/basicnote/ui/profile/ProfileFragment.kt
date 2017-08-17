package com.maheshgaya.android.basicnote.ui.profile

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.User
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.util.openAuthActivity
import com.maheshgaya.android.basicnote.util.signOut
import com.maheshgaya.android.basicnote.util.validateEmail
import com.squareup.picasso.Picasso
import java.lang.Exception

/**
 * Created by Mahesh Gaya on 8/16/17.
 */
class ProfileFragment:Fragment(), View.OnClickListener {

    companion object {
        private val TAG = ProfileFragment::class.simpleName
    }
    private val mAuth = FirebaseAuth.getInstance()
    private var mUser:User? = null
    private val mDatabaseRef = FirebaseDatabase.getInstance()
            .getReference(User.TABLE_NAME + "/" + mAuth.currentUser!!.uid)

    private val mDatabase = mDatabaseRef
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError?) {
                }

                override fun onDataChange(snapshot: DataSnapshot?) {
                    mUser = snapshot?.getValue(User::class.java)
                    setupViews()
                }

            })

    private lateinit var mImageView:ImageView
    private lateinit var mLastNameEditTextInput: TextInputEditText
    private lateinit var mLastNameTextLayout: TextInputLayout
    private lateinit var mFirstNameEditTextInput: TextInputEditText
    private lateinit var mFirstNameTextLayout: TextInputLayout
    private lateinit var mEmailEditTextInput: TextInputEditText
    private lateinit var mEmailTextLayout: TextInputLayout


    init {
        setHasOptionsMenu(true)
        mDatabaseRef.keepSynced(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_profile, container, false)
        mImageView = rootView.bind(R.id.user_imageview)
        mLastNameEditTextInput = rootView.bind(R.id.user_lastname_textedittext)
        mLastNameTextLayout = rootView.bind(R.id.user_lastname_textinputlayout)
        mFirstNameEditTextInput = rootView.bind(R.id.user_firstname_textedittext)
        mFirstNameTextLayout = rootView.bind(R.id.user_firstname_textinputlayout)
        mEmailEditTextInput = rootView.bind(R.id.user_email_textedittext)
        mEmailTextLayout = rootView.bind(R.id.user_email_textinputlayout)
        mImageView.setOnClickListener(this)
        return rootView
    }

    fun setupViews(){
        if (mUser == null) return
        mLastNameEditTextInput.setText(mUser?.lastName)
        mFirstNameEditTextInput.setText(mUser?.firstName)
        mEmailEditTextInput.setText(mUser?.email)
        Picasso
                .with(activity)
                .load(mUser?.imageUrl)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(mImageView)
    }

    private fun updateImage(){
        //TODO remove last image from database if not same
        //save to database
        //save to user
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.user_imageview -> {
                updateImage()
                Toast.makeText(context, "Image Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.profile_menu, menu)
    }

    private fun saveToDatabase(){
        if (!updateUser()){
            Toast.makeText(context, getString(R.string.unsuccessfully_saved), Toast.LENGTH_SHORT).show()
            return
        }

        val updateMap:Map<String, Any?> = mutableMapOf(
                Pair(User.COLUMN_FIRST_NAME, mUser?.firstName),
                Pair(User.COLUMN_LAST_NAME, mUser?.lastName),
                Pair(User.COLUMN_IMAGE_URL, mUser?.imageUrl),
                Pair(User.COLUMN_EMAIL, mUser?.email)
                )
        mDatabaseRef.updateChildren(updateMap)
        Toast.makeText(context, getString(R.string.successfully_saved), Toast.LENGTH_SHORT).show()

        Log.d(TAG, "USER ID= " + mUser?.id)

    }

    private fun validateUI():Boolean{
        var valid = true
        val email = mEmailEditTextInput.text.toString()
        if (email.validateEmail()){
            mEmailTextLayout.error = ""

        } else{
            mEmailTextLayout.error = getString(R.string.email_error)
            valid = false
        }
        Log.d(TAG, "validateUI=" + valid)
        return valid
    }

    private fun updateUser():Boolean{
        if (!validateUI()) return false
        Log.d(TAG, "BEFORE= " + mUser.toString())
        mUser?.firstName = mFirstNameEditTextInput.text.toString()
        mUser?.lastName = mLastNameEditTextInput.text.toString()
        mUser?.email = mEmailEditTextInput.text.toString()
        Log.d(TAG, "AFTER= " + mUser.toString())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item!!.itemId) {
                R.id.action_save -> {
                    saveToDatabase()
                    true
                }
                R.id.action_sign_out -> {
                    signOut()
                    openAuthActivity(activity)
                    true
                }
                android.R.id.home -> {
                    activity.onBackPressed()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }

            }
}