package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/14/17.
 */
class EmptyView:FrameLayout, View.OnClickListener {
    companion object {
        /** For logging purposes */
        private val TAG = EmptyView::class.simpleName
        private val VISIBLE = "visible"
        private val INVISIBLE = "invisible"
        private val GONE = "gone"
    }

    //this should correspond to attrs.xml
    private var mVisibilityMap = mapOf(Pair(GONE, 0), Pair(VISIBLE, 1),Pair(INVISIBLE, 2))
    private var mViewVisibility = mapOf(Pair(0, View.GONE), Pair(1, View.VISIBLE), Pair(2, View.INVISIBLE))

    private var mButtonName = ""
    private var mButtonVisibility: Int = View.VISIBLE

    private var mErrorText = ""
    private var mErrorTextVisibility: Int = View.VISIBLE

    private var mImageSrc:Int = 0
    private var mImageSrcVisibility: Int = View.VISIBLE

    private var mImageView: ImageView
    private var mErrorTextView: TextView
    private var mButton: Button

    interface Callback{
        fun onEmptyButtonClick()
    }
    private var mCallback: Callback? = null

    fun setCallback(listener: Callback){
        mCallback = listener
    }


    /**
     * Invoke constructor
     */
    constructor(context: Context?) : this(context, null)
    /**
     * Invoke constructor
     */
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, R.attr.checkedButtonStyle)

    /**
     * Invoke constructor
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    /**
     * Initialize custom attributes
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        View.inflate(context, R.layout.content_empty, this)
        //initialize views
        mImageView = findViewById(R.id.empty_imageview)
        mButton = findViewById(R.id.empty_button)
        mErrorTextView = findViewById(R.id.empty_textview)

        val a = context!!.obtainStyledAttributes(
                attrs, R.styleable.EmptyView, defStyleAttr, 0
        )

        //App attributes
        if (a.hasValue(R.styleable.EmptyView_buttonName))
            mButtonName = a.getString(R.styleable.EmptyView_buttonName)
        mButtonVisibility = a.getInt(R.styleable.EmptyView_buttonVisibility, mVisibilityMap.getValue(VISIBLE))

        if (a.hasValue(R.styleable.EmptyView_errorText))
            mErrorText = a.getString(R.styleable.EmptyView_errorText)
        mErrorTextVisibility = a.getInt(R.styleable.EmptyView_errorTextVisibility, mVisibilityMap.getValue(VISIBLE))

        mImageSrc = a.getResourceId(R.styleable.EmptyView_imageSrc, android.R.drawable.sym_def_app_icon)
        mImageSrcVisibility = a.getInt(R.styleable.EmptyView_imageSrcVisibility, mVisibilityMap.getValue(VISIBLE))

        a.recycle()

        mImageView.setImageResource(mImageSrc)
        mImageView.visibility = mViewVisibility.getValue(mImageSrcVisibility)

        mErrorTextView.text = mErrorText
        mErrorTextView.visibility = mViewVisibility.getValue(mErrorTextVisibility)

        mButton.text = mButtonName
        mButton.visibility = mViewVisibility.getValue(mButtonVisibility)
        mButton.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        mCallback?.onEmptyButtonClick()
    }
}