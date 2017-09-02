package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.Checkable
import com.maheshgaya.android.basicnote.R


/**
 * Created by Mahesh Gaya on 8/13/17.
 */
class CheckedButton : AppCompatImageView, Checkable {
    /** Handles status of the button */
    private var mChecked = false

    /** Default background colors */
    private val DEFAULT_CHECKED_COLOR = R.color.note_editor_menu_bg_color_dark
    private val DEFAULT_UNCHECKED_COLOR = R.color.note_editor_menu_bg_color

    /** Variables for the backgroun */
    private var mColorChecked = DEFAULT_CHECKED_COLOR
    private var mColorUnchecked = DEFAULT_UNCHECKED_COLOR

    companion object {
        /** For logging purposes */
        private val TAG = CheckedButton::class.simpleName
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
     * Initialize custom attributes
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //Get list of attributes
        val a = context!!.obtainStyledAttributes(
                attrs, R.styleable.CheckedButton, defStyleAttr, 0
        )

        //App attributes
        mChecked = a.getBoolean(R.styleable.CheckedButton_checked, false)
        mColorChecked = a.getColor(R.styleable.CheckedButton_colorChecked, DEFAULT_CHECKED_COLOR)
        mColorUnchecked = a.getColor(R.styleable.CheckedButton_colorUnchecked, DEFAULT_UNCHECKED_COLOR)
        a.recycle()
    }

    /**
     * @return Status of checked
     */
    override fun isChecked(): Boolean = mChecked

    /**
     * Toggle checked
     */
    override fun toggle() {
        isChecked = !mChecked
    }

    /**
     * @param checked Should button be checked
     */
    override fun setChecked(checked: Boolean) {
        //if checked is different from mChecked, refresh the drawable state
        if (mChecked != checked) {
            mChecked = checked
            refreshDrawableState()
        }
    }

    /**
     * Sets the background
     */
    override fun drawableStateChanged() {
        super.drawableStateChanged()
        setBackgroundColor((if (!isChecked) mColorUnchecked else mColorChecked))

    }

    /**
     * @return The button name
     */
    override fun getAccessibilityClassName(): CharSequence = CheckedButton::class.java.name

}


