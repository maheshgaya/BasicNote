package com.maheshgaya.android.basicnote.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.ui.profile.ProfileActivity
import com.maheshgaya.android.basicnote.util.openAuthActivity
import com.maheshgaya.android.basicnote.util.signOut


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    val mAuth = FirebaseAuth.getInstance()
    companion object {
        private val TAG = SettingFragment::class.simpleName
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val signOutPref = findPreference(getString(R.string.pref_key_sign_out))
        signOutPref.summary =  mAuth.currentUser!!.email

    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {

    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean =
        when (preference!!.key){
            getString(R.string.pref_key_profile_activity) -> {
                startActivity(Intent(activity, ProfileActivity::class.java))
                true
            }
            getString(R.string.pref_key_sign_out) -> {
                signOut()
                activity.openAuthActivity()
                true
            }
            else -> {
                super.onPreferenceTreeClick(preference)
            }
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