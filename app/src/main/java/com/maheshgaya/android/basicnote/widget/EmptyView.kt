package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.maheshgaya.android.basicnote.R

/**
 * Created by Mahesh Gaya on 8/14/17.
 */
class EmptyView : FrameLayout, View.OnClickListener {
    companion object {
        /** For logging purposes */
        private val TAG = EmptyView::class.simpleName

        val GONE = 0
        val VISIBLE = 1
        val INVISIBLE = 2
    }

    //this should correspond to attrs.xml

    private var mVisibilityMap = mapOf(Pair(0, View.GONE), Pair(1, View.VISIBLE), Pair(2, View.INVISIBLE))

    var buttonName = ""
        set(value) {
            field = value
            setViews()
        }
    var buttonVisibility: Int = VISIBLE
        set(value) {
            field = validateVisibility(value)
            setViews()
        }

    var errorText = ""
        set(value) {
            field = value
            setViews()
        }
    var errorTextVisibility: Int = VISIBLE
        set(value) {
            field = validateVisibility(value)
            setViews()
        }

    var imageSrc: Int = 0
        set(value) {
            field = value
            setViews()
        }

    var imageSrcVisibility: Int = VISIBLE
        set(value) {
            field = validateVisibility(value)
            setViews()
        }

    private var mImageView: ImageView
    private var mErrorTextView: TextView
    private var mButton: Button

    interface Callback {
        fun onEmptyButtonClick()
    }

    private var mCallback: Callback? = null

    fun setCallback(listener: Callback) {
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
            buttonName = a.getString(R.styleable.EmptyView_buttonName)
        buttonVisibility = a.getInt(R.styleable.EmptyView_buttonVisibility, VISIBLE)
        Log.d(TAG, buttonVisibility.toString())

        if (a.hasValue(R.styleable.EmptyView_errorText))
            errorText = a.getString(R.styleable.EmptyView_errorText)
        errorTextVisibility = a.getInt(R.styleable.EmptyView_errorTextVisibility, VISIBLE)
        Log.d(TAG, errorTextVisibility.toString())

        imageSrc = a.getResourceId(R.styleable.EmptyView_imageSrc, android.R.drawable.sym_def_app_icon)
        imageSrcVisibility = a.getInt(R.styleable.EmptyView_imageSrcVisibility, VISIBLE)
        Log.d(TAG, imageSrcVisibility.toString())

        a.recycle()

        setViews()
        mButton.setOnClickListener(this)

    }

    private fun setViews() {
        mImageView.setImageResource(imageSrc)
        mImageView.tag = imageSrc
        mImageView.visibility = mVisibilityMap.getValue(imageSrcVisibility)

        mErrorTextView.text = errorText
        mErrorTextView.visibility = mVisibilityMap.getValue(errorTextVisibility)

        mButton.text = buttonName
        mButton.visibility = mVisibilityMap.getValue(buttonVisibility)
        invalidate()
    }

    private fun validateVisibility(value: Int): Int =
            when (value) {
                GONE -> GONE
                INVISIBLE -> INVISIBLE
                else -> VISIBLE
            }


    override fun onClick(view: View?) {
        mCallback?.onEmptyButtonClick()
    }
}