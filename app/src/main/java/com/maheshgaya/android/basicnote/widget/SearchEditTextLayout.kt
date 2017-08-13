package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.maheshgaya.android.basicnote.R
import android.text.Editable
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.*


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class SearchEditTextLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), View.OnClickListener {

    companion object {
        private val TAG = SearchEditTextLayout::class.simpleName
    }
    private lateinit var mCollapsedSearchBoxView: View
    private lateinit var mCollapsedDrawerMenu: View
    private lateinit var mCollapsedSearchTextView: TextView
    private lateinit var mCollapsedSearchVoiceView: View
    private lateinit var mCollapsedOverflowView: View

    private lateinit var mExpandedSearchBoxView: View
    private lateinit var mExpandedBackImageView: View
    private lateinit var mExpandedSearchEditText: EditText
    private lateinit var mExpandedSearchVoiceView: ImageView

    private var mIsExpanded = false

    var hintText:String = ""
    set(value) {
        mExpandedSearchEditText.hint = value
        mCollapsedSearchTextView.hint = value
    }

    val fadeIn = AlphaAnimation(0.0f, 1.0f)
    val fadeOut = AlphaAnimation(1.0f, 0.0f)

    init{
        setupViews(false)
    }

    interface Callback {
        fun onMenuButtonClicked()
        fun onBackButtonClicked()
        fun onSearchViewClicked()
        fun onVoiceSearchClicked()
    }

    fun setCallback(listener: Callback) {
        mCallback = listener
    }


    private var mCallback: Callback? = null

    fun isExpandedView(): Boolean = mIsExpanded

    private fun setupViews(moreOptions: Boolean = false) {
        mIsExpanded = false
        View.inflate(context, R.layout.layout_search, this)
        mCollapsedSearchBoxView = findViewById(R.id.search_box_collapsed)
        mCollapsedDrawerMenu = findViewById(R.id.search_box_collapsed_drawer_menu)
        mCollapsedSearchTextView = findViewById(R.id.search_box_collapsed_textview)
        mCollapsedSearchVoiceView = findViewById(R.id.search_box_collapsed_voice)

        mCollapsedOverflowView = findViewById(R.id.search_box_collapsed_overflow)
        if (!moreOptions) mCollapsedOverflowView.visibility = View.GONE

        mCollapsedDrawerMenu.setOnClickListener(this)
        mCollapsedSearchTextView.setOnClickListener(this)


        mExpandedSearchBoxView = findViewById(R.id.search_box_expanded)
        mExpandedBackImageView = findViewById(R.id.search_box_expanded_back)
        mExpandedSearchEditText = findViewById(R.id.search_box_expanded_edittext)
        mExpandedSearchVoiceView = findViewById(R.id.search_box_expanded_voice)

        mExpandedBackImageView.setOnClickListener(this)
        mExpandedSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                mExpandedSearchVoiceView.setImageResource(
                        if (text.isEmpty()) R.drawable.ic_search_box_mic else R.drawable.ic_close)
                mExpandedSearchVoiceView.tag =
                        if (text.isEmpty()) context.getString(R.string.tag_mic)
                        else context.getString(R.string.tag_close)
            }

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(text: Editable) {

            }
        })
        mExpandedSearchVoiceView.setOnClickListener(this)


    }

    fun expandView(value: Boolean){
        if (value){
            mIsExpanded = true
            mExpandedSearchBoxView.animation = fadeIn
            mExpandedSearchBoxView.visibility = View.VISIBLE
            mExpandedSearchBoxView.animation = fadeOut
            mCollapsedSearchBoxView.visibility = View.GONE
            mExpandedSearchEditText.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        } else{
            mIsExpanded = false
            mExpandedSearchBoxView.visibility = View.GONE
            mCollapsedSearchBoxView.visibility = View.VISIBLE
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mExpandedSearchEditText.windowToken, 0)
        }
    }



    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.search_box_collapsed_drawer_menu -> {
                Log.d(TAG, "search_box_collapsed_drawer_menu")
                mCallback!!.onMenuButtonClicked()

            }
            R.id.search_box_collapsed_textview -> {
                expandView(true)
                mCallback!!.onSearchViewClicked()

            }
            R.id.search_box_expanded_back -> {
                expandView(false)
                mCallback!!.onBackButtonClicked()
            }
            R.id.search_box_expanded_voice -> {
                if (mExpandedSearchVoiceView.tag == context.getString(R.string.tag_mic)) {
                    mCallback!!.onVoiceSearchClicked()
                } else {
                    mExpandedSearchEditText.text.clear()
                }
            }
        }

    }



}