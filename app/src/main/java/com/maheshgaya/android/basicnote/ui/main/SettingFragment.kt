package com.maheshgaya.android.basicnote.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.maheshgaya.android.basicnote.R


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.pref_notes, rootKey)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        setHasOptionsMenu(true)
    }


    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }


}