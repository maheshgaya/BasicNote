package com.maheshgaya.android.basicnote.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maheshgaya.android.basicnote.Constants
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.User
import com.maheshgaya.android.basicnote.ui.auth.AuthActivity
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.util.signOut
import com.maheshgaya.android.basicnote.widget.SearchEditTextLayout
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        SearchEditTextLayout.Callback {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigationView: NavigationView

    private lateinit var mSearchView: SearchEditTextLayout
    private lateinit var mToolbar: Toolbar

    private lateinit var mFAB: FloatingActionButton

    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance()


    companion object {
        private val TAG = MainActivity::class.simpleName
        private val FRAG_ID = "frag_main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDrawer = bind(R.id.drawer_layout)

        mSearchView = bind(R.id.search_edittextlayout)
        mSearchView.setCallback(this)
        mToolbar = bind(R.id.toolbar)
        mFAB = bind(R.id.fab_main)

        mNavigationView = bind(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)

        if (supportFragmentManager.findFragmentByTag(FRAG_ID) == null) {
            onNavigationItemSelected(mNavigationView.menu.findItem(R.id.nav_notes))
        }

        setupUserProfile()

    }

    fun setupUserProfile(){
        val userNameTextView:TextView = mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_name_textview)
        val userEmailTextView:TextView = mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_email_textview)
        val userImageView:ImageView = mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_imageview)
        val user = mAuth.currentUser


        val ref = mDatabase.getReference(Constants.USER_TABLE + "/" + user!!.uid)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val currentUser = dataSnapshot.getValue<User>(User::class.java)
                val name = currentUser!!.firstName + " " + currentUser.lastName
                Log.d(TAG, currentUser.toString())
                userNameTextView.setText(name)
                userEmailTextView.text = user.email
                Picasso
                        .with(this@MainActivity)
                        .load(currentUser.imageUrl)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(userImageView)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }

        ref.addValueEventListener(postListener)

    }

    override fun onMenuButtonClicked() {
        Log.d(TAG, "onMenuButtonClicked")
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START)
        } else{
            mDrawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onSearchViewClicked() {
        Log.d(TAG, "onSearchViewClicked")
    }

    override fun onBackButtonClicked() {
        Log.d(TAG, "onBackButtonClicked")
    }

    override fun onVoiceSearchClicked() {
        Log.d(TAG, "onVoiceSearchClicked")
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




    override fun onDestroy() {
        supportFinishAfterTransition()
        super.onDestroy()
    }

    private fun openAuthActivity(){
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_sign_out){
            signOut()
            openAuthActivity()
            return true
        }

        val fragmentList =
                mapOf(Pair(R.id.nav_settings, SettingFragment::class.java),
                        Pair(R.id.nav_trash, TrashFragment::class.java),
                        Pair(R.id.nav_notes, NoteFragment::class.java))
        // Handle navigation view item clicks here.
        val fragment = fragmentList[item.itemId]!!.newInstance()

        showSearchToolbar(item)

        supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, fragment, FRAG_ID).commit()
        // Highlight the selected item has been done by NavigationView
        item.isChecked = true
        // Set action bar title
        mToolbar.title = item.title
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun showSearchToolbar(item: MenuItem){

        if (item.itemId != R.id.nav_notes && item.itemId != R.id.nav_trash) {
            mToolbar.visibility =  View.VISIBLE
            mSearchView.visibility = View.GONE

            setSupportActionBar(mToolbar)
            val toggle = ActionBarDrawerToggle(
                    this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            mDrawer.addDrawerListener(toggle)
            toggle.syncState()

        } else {
            mToolbar.visibility = View.GONE
            mSearchView.visibility = View.VISIBLE
            mSearchView.hintText =
                    if (item.itemId == R.id.nav_trash) getString(R.string.hint_search_trash)
                    else getString(R.string.hint_search_notes)
        }

        mFAB.visibility = if (item.itemId == R.id.nav_settings){ View.GONE } else { View.VISIBLE }
    }
}
