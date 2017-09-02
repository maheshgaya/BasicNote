package com.maheshgaya.android.basicnote.ui.profile

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.User
import com.maheshgaya.android.basicnote.util.openAuthActivity
import com.maheshgaya.android.basicnote.util.showSnackbar
import com.maheshgaya.android.basicnote.util.signOut
import com.squareup.picasso.Picasso

/**
 * Created by Mahesh Gaya on 8/16/17.
 */
class ProfileFragment : Fragment(), View.OnClickListener {

    companion object {
        private val TAG = ProfileFragment::class.simpleName
    }

    private val mAuth = FirebaseAuth.getInstance()
    private var mUser: User? = null
    private val mDatabaseRef = FirebaseDatabase.getInstance()
            .getReference(User.TABLE_NAME + "/" + mAuth.currentUser!!.uid)

    private val mDatabaseValueListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {}

        override fun onDataChange(snapshot: DataSnapshot?) {
            mUser = snapshot?.getValue(User::class.java)
            setupViews()
        }
    }

    private lateinit var mImageView: ImageView
    private lateinit var mLastNameEditText: EditText
    private lateinit var mFirstNameEditText: EditText
    private lateinit var mEmailEditText: EditText
    private lateinit var mBackgroundImageView: ImageView
    private lateinit var mCoordinatorLayout: CoordinatorLayout

    init {
        setHasOptionsMenu(true)
        mDatabaseRef.keepSynced(true)

    }

    override fun onResume() {
        super.onResume()
        mDatabaseRef.addValueEventListener(mDatabaseValueListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_profile, container, false)
        mLastNameEditText = rootView!!.findViewById(R.id.user_lastname_textedittext)
        mFirstNameEditText = rootView.findViewById(R.id.user_firstname_textedittext)
        mEmailEditText = rootView.findViewById(R.id.user_email_textedittext)

        mImageView = rootView.findViewById(R.id.user_imageview)
        mBackgroundImageView = rootView.findViewById(R.id.profile_background_imageview)
        mImageView.setOnClickListener(this)
        return rootView
    }

    override fun onPause() {
        super.onPause()
        mDatabaseRef.removeEventListener(mDatabaseValueListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCoordinatorLayout = activity.findViewById(R.id.coordinator)
    }

    private fun setupViews() {

        if (mUser == null) return
        val lastName = mUser?.lastName ?: ""
        val firstName = mUser?.firstName ?: ""
        val email = mUser?.email ?: ""

        mLastNameEditText.setText(lastName)
        mFirstNameEditText.setText(firstName)
        mEmailEditText.setText(email)

        val image = mUser?.imageUrl ?: ""
        Picasso
                .with(activity)
                .load(image)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(mImageView)
        Picasso
                .with(activity)
                .load(image)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(mBackgroundImageView)

    }

    private fun updateImage() {
        //TODO remove last image from database if not same
        //save to database
        //save to user
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.user_imageview -> {
                updateImage()
                mCoordinatorLayout.showSnackbar("Image Clicked")
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.profile_menu, menu)
    }

    private fun saveToDatabase() {
        if (!updateUser()) {
            return
        }

        val updateMap: Map<String, Any?> = mutableMapOf(
                Pair(User.COLUMN_FIRST_NAME, mUser?.firstName),
                Pair(User.COLUMN_LAST_NAME, mUser?.lastName),
                Pair(User.COLUMN_IMAGE_URL, mUser?.imageUrl)
        )
        mDatabaseRef.updateChildren(updateMap)
        mCoordinatorLayout.showSnackbar(getString(R.string.successfully_saved))
    }

    private fun updateUser(): Boolean {
        mUser?.firstName = mFirstNameEditText.text?.toString() ?: ""
        mUser?.lastName = mLastNameEditText.text?.toString() ?: ""
        mUser?.email = mEmailEditText.text?.toString() ?: ""
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
                    activity.openAuthActivity()
                    true
                }
                android.R.id.home -> {
                    activity.supportFinishAfterTransition()
                    activity.onBackPressed()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }

            }
}