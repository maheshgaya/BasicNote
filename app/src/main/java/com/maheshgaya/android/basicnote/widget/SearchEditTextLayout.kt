package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SearchEditTextLayout : FrameLayout, View.OnClickListener {

    private lateinit var mCollapsedSearchBoxView:View
    private lateinit var mCollapsedDrawerMenu: View
    private lateinit var mCollapsedSearchTextView: View
    private lateinit var mCollapsedSearchVoiceView: View
    private lateinit var mCollapsedOverflowView: View

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs){
        setupViews(false)
    }

    constructor(context: Context?, attrs: AttributeSet?, moreOptions:Boolean): super(context, attrs){
        setupViews(moreOptions)
    }

    private var mCallback: Callback? = null

    private fun setupViews(moreOptions: Boolean){
        View.inflate(context, R.layout.layout_search, this)
        mCollapsedSearchBoxView = findViewById(R.id.search_box_collapsed)
        mCollapsedDrawerMenu = findViewById(R.id.search_box_collapsed_drawer_menu)
        mCollapsedSearchTextView = findViewById(R.id.search_box_collapsed_textview)
        mCollapsedSearchVoiceView = findViewById(R.id.search_box_collapsed_voice)

        mCollapsedOverflowView = findViewById(R.id.search_box_collapsed_overflow)
        if (!moreOptions) mCollapsedOverflowView.visibility = View.GONE

        mCollapsedDrawerMenu.setOnClickListener(this)
    }
    interface Callback {
        fun onBackButtonClicked()
        fun onSearchViewClicked()
    }

    fun setCallback(listener: Callback){
        mCallback = listener
    }



    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.search_box_collapsed_drawer_menu -> {
                mCallback!!.onBackButtonClicked()

            }
            R.id.search_box_collapsed_textview -> {
                Toast.makeText(context, "EditText Opened", Toast.LENGTH_SHORT).show()
            }
        }

    }


}