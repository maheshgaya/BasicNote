package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.google.firebase.database.ServerValue
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.bind
import com.maheshgaya.android.basicnote.widget.SearchEditTextLayout

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        SearchEditTextLayout.Callback {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigationView: NavigationView

    private lateinit var mSearchView:SearchEditTextLayout
    private lateinit var mToolbar:Toolbar
    companion object {
        private val TAG = MainActivity::class.simpleName
        private val FRAG_ID = "CURRENT_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDrawer = bind(R.id.drawer_layout)

        mSearchView = bind(R.id.search_edittextlayout)
        mSearchView.setCallback(this)
        mToolbar = bind(R.id.toolbar)

        mNavigationView = bind(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)

        if (supportFragmentManager.findFragmentByTag(FRAG_ID) == null) {
            onNavigationItemSelected(mNavigationView.menu.findItem(R.id.nav_notes))
        }

    }

    override fun onBackButtonClicked() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START)
        } else{
            mDrawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onSearchViewClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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
        title = item.title
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun showSearchToolbar(item: MenuItem){

        if (item.itemId != R.id.nav_notes) {
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
        }
    }
}
