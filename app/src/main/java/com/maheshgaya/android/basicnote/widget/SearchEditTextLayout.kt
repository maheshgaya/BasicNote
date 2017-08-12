package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SearchEditTextLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {

    private var mCollapsedSearchBoxView:View
    private var mCollapsedDrawerMenu: View
    private var mCollapsedSearchTextView: View
    private var mCollapsedSearchVoiceView: View
    private var mCollapsedMoreOptionsView: View


    private lateinit var mCallback: Callback

    interface Callback {
        fun onBackButtonClicked()
        fun onSearchViewClicked()
    }

    fun setCallback(listener: Callback){
        mCallback = listener
    }

    init {
        View.inflate(context, R.layout.search_layout, this)
        mCollapsedSearchBoxView = findViewById(R.id.search_box_collapsed)
        mCollapsedDrawerMenu = findViewById(R.id.search_box_collapsed_drawer_menu)
        mCollapsedSearchTextView = findViewById(R.id.search_box_collapsed_textview)
        mCollapsedSearchVoiceView = findViewById(R.id.search_box_collapsed_voice)
        mCollapsedMoreOptionsView = findViewById(R.id.search_box_collapsed_more_options)

        mCollapsedDrawerMenu.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.search_box_collapsed_drawer_menu -> {
                if (mCallback != null) {
                    mCallback.onBackButtonClicked()
                }
            }
        }

    }

}