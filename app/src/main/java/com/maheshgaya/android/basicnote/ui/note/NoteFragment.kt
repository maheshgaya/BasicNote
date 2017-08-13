package com.maheshgaya.android.basicnote.ui.note

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spannable
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.util.bind


/**
 * Created by Mahesh Gaya on 8/12/17.
 */
class NoteFragment: Fragment() {
    private lateinit var mToolbar: Toolbar
    private lateinit var mBodyEditText: EditText
    companion object {
        private val TAG = NoteFragment::class.simpleName
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_note, container, false)
        mBodyEditText = rootView.findViewById(R.id.note_edittext)
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mToolbar = activity.bind(R.id.toolbar)
        (activity as NoteActivity).setSupportActionBar(mToolbar)
        (activity as NoteActivity).supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        (activity as NoteActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as NoteActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        (activity as NoteActivity).supportActionBar!!.title = ""

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item!!.itemId) {
            R.id.action_share -> {

                val positionStart = mBodyEditText.selectionStart
                val positionEnd = mBodyEditText.selectionEnd
                mBodyEditText.text.setSpan(StyleSpan(Typeface.BOLD), positionStart, positionEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                val style = mBodyEditText.text.getSpans(positionStart, positionEnd, StyleSpan::class.java)

                if(style[0].style == Typeface.BOLD){
                    Toast.makeText(context, style[0].toString(), Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.d(TAG, Html.toHtml(mBodyEditText.text, 0).toString())
                    } else {
                        Log.d(TAG,Html.toHtml(mBodyEditText.text).toString())
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }

    inner class Callback() : ActionMode.Callback{
        override fun onActionItemClicked(mode: ActionMode?, menuItem: MenuItem?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCreateActionMode(mode: ActionMode?, menuItem: Menu?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPrepareActionMode(mode: ActionMode?, menuItem: Menu?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}