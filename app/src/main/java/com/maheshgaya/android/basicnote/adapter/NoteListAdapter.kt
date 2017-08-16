package com.maheshgaya.android.basicnote.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note
import com.maheshgaya.android.basicnote.ui.note.NoteActivity
import com.maheshgaya.android.basicnote.ui.note.NoteFragment
import com.maheshgaya.android.basicnote.util.fromHtml
import com.maheshgaya.android.basicnote.util.toDate
import com.maheshgaya.android.basicnote.util.toLastedEditedDate

/**
 * Created by Mahesh Gaya on 8/13/17.
 */
//TODO Test this class
class NoteListAdapter(context: Context?, list:MutableList<Note>): RecyclerView.Adapter<NoteListAdapter.ViewHolder>(), View.OnClickListener {

    /** mutable list of notes */
    private var mList = list
    /** context */
    private var mContext = context


    init {
        setHasStableIds(true)
    }

    /**
     * bind the view holder
     */
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        //sets title and body for the corresponding note
        holder?.setNote(mList[position])
        //set onClickListener
        holder?.noteCardView?.setOnClickListener(this)
        holder?.noteCardView?.tag = position
    }

    override fun getItemCount(): Int = mList.size

    fun getItem(position: Int): Note = mList[position]

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    /**
     * Creates the view holder by inflating the list item layout
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        // Inflate the custom layout and return a new holder instance

        return ViewHolder(inflater.inflate(R.layout.list_item_note, parent, false))
    }

    /**
     * Handles onClick events
     */
    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.item_note_cardview -> {
                val intent = Intent(mContext as Activity, NoteActivity::class.java)
                val position= view.tag as Int
                intent.putExtra(NoteFragment.NOTE_KEY, mList[position])
                mContext!!.startActivity(intent)

            }
        }
    }


    /**
     * ViewHolder to contain the item view
     */
    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        private var context: Context = itemView!!.context
        /** Outer layout of the card */
        var noteCardView:View = itemView!!.findViewById(R.id.item_note_cardview)
        /** Title textview */
        var noteTitleTextView:TextView = itemView!!.findViewById(R.id.item_note_title)
        /** Body textview */
        var noteBodyTextView:TextView = itemView!!.findViewById(R.id.item_note_body)
        /** Last edited */
        var lastEditedTextView: TextView = itemView!!.findViewById(R.id.item_note_last_edited)

        /**
         * Sets text for note's title and body
         * @param note Note model
         */
        fun setNote(note: Note){
            noteTitleTextView.text = fromHtml(note.title, mode = Html.FROM_HTML_MODE_COMPACT).trim()
            val body = fromHtml(note.body).trim()
            noteBodyTextView.text = body
            noteBodyTextView.hint = if (body.isNullOrEmpty()) mContext?.getString(R.string.no_body_text) else ""
            lastEditedTextView.text = note.lastEdited.toDate(context = context)
        }

    }
}