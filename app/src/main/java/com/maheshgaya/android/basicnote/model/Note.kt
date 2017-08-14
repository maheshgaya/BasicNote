package com.maheshgaya.android.basicnote.model

import android.os.Parcel
import android.os.Parcelable
import com.maheshgaya.android.basicnote.Constants

/**
 * Created by Mahesh Gaya on 8/12/17.
 */


data class Note(var id: String? = null, var title: String = "",
                var body: String = "", var tags: String? = ""):Parcelable{

    companion object {
        val TABLE_NAME = Constants.NOTE_TABLE

        @JvmField @Suppress("unused")
        val CREATOR: Parcelable.Creator<Note> = object : Parcelable.Creator<Note> {

            // This simply calls our new constructor (typically private) and
            // passes along the unmarshalled `Parcel`, and then returns the new object!
            override fun createFromParcel(parcelIn: Parcel): Note {
                return Note(parcelIn)
            }

            // We just need to copy this and change the type to match our class.
            override fun newArray(size: Int): Array<Note?> {
                return arrayOfNulls(size)
            }
        }
    }

    private constructor(parcelIn:Parcel) : this(
            id = parcelIn.readString(),
            title = parcelIn.readString(),
            body = parcelIn.readString(),
            tags = parcelIn.readString()
            )
    override fun writeToParcel(out: Parcel?, flags: Int) {
        out!!.writeString(id)
        out.writeString(title)
        out.writeString(body)
        out.writeString(tags)
    }

    override fun describeContents(): Int = 0

}