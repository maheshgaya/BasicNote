package com.maheshgaya.android.basicnote.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.ui.main.MainActivity

/**
 * Created by Mahesh Gaya on 8/11/17.
 */
class AuthActivity : AppCompatActivity(){
    companion object {
        private val TAG = AuthActivity::class.simpleName
        val FRAG_ID = "fragment_auth"
        val mFragmentList = mapOf(Pair(R.layout.fragment_sign_in, SignInFragment::class.java),
                Pair(R.layout.fragment_sign_up, SignUpFragment::class.java))
    }

    fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val fragment = mFragmentList[R.layout.fragment_sign_in]!!.newInstance() as Fragment
        supportFragmentManager.beginTransaction().replace(R.id.framelayout_auth, fragment, FRAG_ID).commit()
    }



}