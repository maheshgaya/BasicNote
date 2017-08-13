package com.maheshgaya.android.basicnote.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.maheshgaya.android.basicnote.R
import com.maheshgaya.android.basicnote.model.Note

/**
 * Created by Mahesh Gaya on 8/13/17.
 */
class NoteListAdapter(list:MutableList<Note>): RecyclerView.Adapter<NoteListAdapter.ViewHolder>(), View.OnClickListener {

    private var mList = list

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.setNote(mList[position])
        holder.noteCard.setOnClickListener(this)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        // Inflate the custom layout and return a new holder instance
        return ViewHolder(inflater.inflate(R.layout.list_item_note, parent, false))
    }

    override fun onClick(view: View?) {
        when (view!!.id){
            R.id.item_note_cardview -> {
                //TODO Open NoteActivity
            }
        }
    }


    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var noteCard:View = itemView!!.findViewById(R.id.item_note_cardview)
        var noteTitle:TextView = itemView!!.findViewById(R.id.item_note_title)
        var noteBody:TextView = itemView!!.findViewById(R.id.item_note_body)

        fun setNote(note: Note){
            noteTitle.text = note.title
            noteBody.text = note.body
        }

    }
}