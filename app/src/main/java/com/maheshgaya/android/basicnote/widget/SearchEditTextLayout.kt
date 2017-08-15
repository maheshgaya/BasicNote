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
        /** Tag for logging purposes */
        private val TAG = SearchEditTextLayout::class.simpleName
    }
    /** Collapsed outer layout */
    private lateinit var mCollapsedSearchBoxView: View
    /** Collapsed drawer menu (hamburger icon) */
    private lateinit var mCollapsedDrawerMenu: View
    /** Collapsed search textview shows the query hint */
    private lateinit var mCollapsedSearchTextView: TextView
    /** Collapsed voice icon */
    private lateinit var mCollapsedSearchVoiceView: View
    /** Collapsed overflow view for more options */
    private lateinit var mCollapsedOverflowView: View

    /** Expanded outer layout */
    private lateinit var mExpandedSearchBoxView: View
    /** Expanded back button */
    private lateinit var mExpandedBackImageView: View
    /** Expanded search edit text */
    private lateinit var mExpandedSearchEditText: EditText
    /** Expanded voice icon */
    private lateinit var mExpandedSearchVoiceView: ImageView

    /** If expanded search view is active or not */
    private var mIsExpanded = false

    /** Constants for search view transition */
    private val fadeIn = AlphaAnimation(0.0f, 1.0f)
    private val fadeOut = AlphaAnimation(1.0f, 0.0f)

    /** Query hint */
    var hintText:String = ""
        set(value) {
            //Set hint for the collapsed and expanded search view
            mExpandedSearchEditText.hint = value
            mCollapsedSearchTextView.hint = value
        }



    init{
        //initialize the views
        setupViews(false)
    }

    /** handles onClicked for the buttons/views */
    interface Callback {
        /**
         * menu button is clicked
         */
        fun onMenuButtonClicked()

        /**
         * back button is clicked
         */
        fun onBackButtonClicked()

        /**
         * searchview is clicked
         */
        fun onSearchViewClicked()

        /**
         * voice view is clicked
         */
        fun onVoiceSearchClicked()

        /**
         * SearchQuery
         */
        fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int)
    }

    /** callback for the listeners */
    private var mCallback: Callback? = null

    /**
     * initializes the listeners
     */
    fun setCallback(listener: Callback) {
        mCallback = listener
    }

    /** check if expandedview is active or not */
    fun isExpandedView(): Boolean = mIsExpanded

    /**
     * initializes the views
     */
    private fun setupViews(moreOptions: Boolean = false) {
        //make sure that the expanded view is false
        mIsExpanded = false
        //inflate custom layout
        View.inflate(context, R.layout.layout_search, this)

        //for collapsed views
        mCollapsedSearchBoxView = findViewById(R.id.search_box_collapsed)
        mCollapsedDrawerMenu = findViewById(R.id.search_box_collapsed_drawer_menu)
        mCollapsedSearchTextView = findViewById(R.id.search_box_collapsed_textview)
        mCollapsedSearchVoiceView = findViewById(R.id.search_box_collapsed_voice)

        //currently not using this, so set visibility GONE
        mCollapsedOverflowView = findViewById(R.id.search_box_collapsed_overflow)
        if (!moreOptions) mCollapsedOverflowView.visibility = View.GONE

        //handles drawer navigation menu
        mCollapsedDrawerMenu.setOnClickListener(this)
        //handles searchview
        mCollapsedSearchTextView.setOnClickListener(this)

        //for expanded views
        mExpandedSearchBoxView = findViewById(R.id.search_box_expanded)
        mExpandedBackImageView = findViewById(R.id.search_box_expanded_back)
        mExpandedSearchEditText = findViewById(R.id.search_box_expanded_edittext)
        mExpandedSearchVoiceView = findViewById(R.id.search_box_expanded_voice)

        //handles back for expanded views
        mExpandedBackImageView.setOnClickListener(this)
        //listens to text changes in search edittext
        mExpandedSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                //change the icons accordingly, set `mic` or `clear text`
                mExpandedSearchVoiceView.setImageResource(
                        if (text.isEmpty()) R.drawable.ic_search_box_mic else R.drawable.ic_close)
                mExpandedSearchVoiceView.tag =
                        if (text.isEmpty()) context.getString(R.string.tag_mic)
                        else context.getString(R.string.tag_close)
                mCallback?.onTextChanged(text, start, before, count)
            }

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                //Not implemented
            }

            override fun afterTextChanged(text: Editable) {
                //Not implemented
            }
        })
        //Handles on voice command for expanded searchview
        mExpandedSearchVoiceView.setOnClickListener(this)

    }

    /**
     * Expands the searchview if value is true
     * @param expand Show expanded view
     */
    fun expandView(expand: Boolean){
        if (expand){
            mIsExpanded = true
            //set animation and hide collapsed searchview
            mExpandedSearchBoxView.animation = fadeIn
            mExpandedSearchBoxView.visibility = View.VISIBLE
            mExpandedSearchBoxView.animation = fadeOut
            mCollapsedSearchBoxView.visibility = View.GONE

            //request focus and show soft keyboard
            mExpandedSearchEditText.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        } else{
            mIsExpanded = false
            //hide expanded searchview
            mExpandedSearchBoxView.visibility = View.GONE
            mCollapsedSearchBoxView.visibility = View.VISIBLE
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mExpandedSearchEditText.windowToken, 0)
        }
    }

    /**
     * Handles onClick
     * @param view UI view
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.search_box_collapsed_drawer_menu -> {
                //sets callback to open or close the drawer navigation menu
                mCallback?.onMenuButtonClicked()

            }
            R.id.search_box_collapsed_textview -> {
                //expands the searchview and sets callback
                expandView(true)
                mCallback?.onSearchViewClicked()

            }
            R.id.search_box_expanded_back -> {
                //collapsed the searchview and sets callback
                clearText()
                expandView(false)
                mCallback?.onBackButtonClicked()
            }
            R.id.search_box_expanded_voice -> {
                //set callback for voice command or clear text in expanded search edittext
                if (mExpandedSearchVoiceView.tag == context.getString(R.string.tag_mic)) {
                    mCallback?.onVoiceSearchClicked()
                } else {
                    clearText()
                }
            }
        }

    }

    fun clearText() {
        mExpandedSearchEditText.text.clear()
    }


}