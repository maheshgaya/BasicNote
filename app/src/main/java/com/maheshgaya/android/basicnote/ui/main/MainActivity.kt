package com.maheshgaya.android.basicnote.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
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
import com.maheshgaya.android.basicnote.ui.note.NoteActivity
import com.maheshgaya.android.basicnote.ui.note.NoteFragment
import com.maheshgaya.android.basicnote.ui.search.SearchResultFragment
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.util.signOut
import com.maheshgaya.android.basicnote.widget.SearchEditTextLayout
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        SearchEditTextLayout.Callback, View.OnClickListener {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigationView: NavigationView

    private lateinit var mSearchView: SearchEditTextLayout
    private lateinit var mToolbar: Toolbar

    private lateinit var mFAB: FloatingActionButton

    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance()

    private val mSearchResultFragment = SearchResultFragment::class.java.newInstance()
    private var mFragment: Fragment? = null

    val mFragmentList =
            mapOf(Pair(R.id.nav_settings, SettingFragment::class.java),
                    Pair(R.id.nav_trash, TrashFragment::class.java),
                    Pair(R.id.nav_notes, NoteListFragment::class.java))

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
        mToolbar.visibility = View.GONE
        mFAB = bind(R.id.fab_main)
        mFAB.setOnClickListener(this)
        mNavigationView = bind(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)

        if (supportFragmentManager.findFragmentByTag(FRAG_ID) == null) {
            onNavigationItemSelected(mNavigationView.menu.findItem(R.id.nav_notes))
        } else {
            when (supportFragmentManager.fragments[0]) {
                is NoteListFragment -> {
                    showSearchToolbar(mNavigationView.menu.findItem(R.id.nav_notes))
                }
                is TrashFragment -> {
                    showSearchToolbar(mNavigationView.menu.findItem(R.id.nav_trash))
                }
                is SettingFragment -> {
                    showSearchToolbar(mNavigationView.menu.findItem(R.id.nav_settings))
                }
            }
        }

        setupUserProfile()

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.fab_main -> {
                startActivity(Intent(this@MainActivity, NoteActivity::class.java))
            }
        }
    }

    private fun setupUserProfile() {
        val userNameTextView: TextView = mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_name_textview)
        val userEmailTextView: TextView = mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_email_textview)
        val userImageView: ImageView = mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_imageview)
        val user = mAuth.currentUser


        val ref = mDatabase.getReference(Constants.USER_TABLE + "/" + user!!.uid)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val currentUser = dataSnapshot.getValue<User>(User::class.java)
                val name = currentUser!!.firstName + " " + currentUser.lastName
                Log.d(TAG, currentUser.toString())
                userNameTextView.text = name
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
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            mDrawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        mSearchResultFragment.searchQuery = text.toString()
    }

    override fun onSearchViewClicked() {
        mFAB.visibility = View.GONE
        setSearchResultView(true)
    }

    override fun onBackButtonClicked() {
        mFAB.visibility = View.VISIBLE
        setSearchResultView(false)
    }

    override fun onVoiceSearchClicked() {
        Log.d(TAG, "onVoiceSearchClicked")
    }

    private fun setSearchResultView(value: Boolean) {
        val fragment: Fragment?
        mSearchResultFragment.clearList()
        if (value) {
            fragment = mSearchResultFragment
        } else {
            fragment = mFragment
        }


        mSearchResultFragment.mainSearch = mFragment !is TrashFragment
        Log.d(TAG + ":mainSearch", mSearchResultFragment.mainSearch.toString())
        supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, fragment, FRAG_ID).commit()
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else if (mSearchView.isExpandedView()) {
            mSearchView.clearText()
            mSearchView.expandView(false)
            setSearchResultView(false)
        } else {
            super.onBackPressed()
        }
    }


    private fun openAuthActivity() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_sign_out) {
            signOut()
            openAuthActivity()
            return true
        }

        // Handle navigation view item clicks here.
        mFragment = mFragmentList[item.itemId]!!.newInstance() as Fragment?

        showSearchToolbar(item)

        supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, mFragment, FRAG_ID).commit()
        // Highlight the selected item has been done by NavigationView
        item.isChecked = true
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showSearchToolbar(item: MenuItem) {
        // Set action bar title
        mToolbar.title = item.title
        val value =
                when (item.itemId) {
                    R.id.nav_notes -> {
                        mSearchView.hintText = getString(R.string.hint_search_notes)
                        true
                    }
                    R.id.nav_trash -> {
                        mSearchView.hintText = getString(R.string.hint_search_trash)
                        true
                    }
                    else -> {
                        false
                    }
                }

        if (value) {
            mToolbar.visibility = View.GONE
            mSearchView.visibility = View.VISIBLE

        } else {
            mToolbar.visibility = View.VISIBLE
            mSearchView.visibility = View.GONE

            setSupportActionBar(mToolbar)
            val toggle = ActionBarDrawerToggle(
                    this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            mDrawer.addDrawerListener(toggle)
            toggle.syncState()
        }

        mFAB.visibility = if (item.itemId == R.id.nav_settings) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


}
