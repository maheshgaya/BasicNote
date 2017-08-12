package com.maheshgaya.android.basicnote.ui.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.bind

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mToolbar: Toolbar
    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigationView: NavigationView

    companion object {
        private val TAG = MainActivity::class.simpleName
        private val FRAG_ID = "CURRENT_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar = bind(R.id.toolbar)
        setSupportActionBar(mToolbar)

        mDrawer = bind(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()

        mNavigationView = bind(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)

        if (supportFragmentManager.findFragmentByTag(FRAG_ID) == null) {
            onNavigationItemSelected(mNavigationView.menu.findItem(R.id.nav_notes))
        }

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

        supportFragmentManager.beginTransaction().replace(R.id.framelayout_main, fragment, FRAG_ID).commit()
        // Highlight the selected item has been done by NavigationView
        item.isChecked = true
        // Set action bar title
        title = item.title
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }
}
