package com.maheshgaya.android.basicnote.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.Checkable
import com.maheshgaya.android.basicnote.R


/**
 * Created by Mahesh Gaya on 8/13/17.
 */
class CheckedButton : AppCompatImageView, Checkable{
    private var mChecked = false
    private val DEFAULT_CHECKED_COLOR = R.color.note_editor_menu_bg_color_dark
    private val DEFAULT_UNCHECKED_COLOR = R.color.note_editor_menu_bg_color
    private var mColorChecked = DEFAULT_CHECKED_COLOR
    private var mColorUnchecked = DEFAULT_UNCHECKED_COLOR

    companion object {
        private val TAG = CheckedButton::class.simpleName
    }

    constructor(context: Context?):this(context, null)
    constructor(context: Context?, attrs: AttributeSet?):this(context, attrs, R.attr.checkedButtonStyle)
    constructor(context: Context?, attrs: AttributeSet? , defStyleAttr: Int):super(context, attrs, defStyleAttr){
        //get list of attributes
        val a = context!!.obtainStyledAttributes(
                attrs, R.styleable.CheckedButton, defStyleAttr, 0
        )

        mChecked = a.getBoolean(R.styleable.CheckedButton_checked, false)
        mColorChecked = a.getColor(R.styleable.CheckedButton_colorChecked, DEFAULT_CHECKED_COLOR)
        mColorUnchecked = a.getColor(R.styleable.CheckedButton_colorUnchecked, DEFAULT_UNCHECKED_COLOR)
        a.recycle()
    }

    override fun isChecked(): Boolean = mChecked

    override fun toggle() {
        isChecked = !mChecked
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked != checked){
            mChecked = checked
            refreshDrawableState()
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        setBackgroundColor((if (!isChecked) mColorUnchecked else mColorChecked))

    }


    override fun getAccessibilityClassName(): CharSequence {
        return CheckedButton::class.java.name
    }


}


