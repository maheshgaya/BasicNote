package com.maheshgaya.android.basicnote.model

import android.os.Parcel
import android.os.Parcelable
import com.maheshgaya.android.basicnote.Constants

/**
 * Created by Mahesh Gaya on 8/12/17.
 */


/**
 * The Note model
 * @param uid User ID (From Firebase)
 * @param title Title text of the note
 * @param body Body text of the note
 * @param tags Tags for the note
 */
data class Note(var id: String? = null, var uid: String? = null, var title: String = "",
                var body: String = "", var tags: String? = "",
                var lastEdited:Long = System.currentTimeMillis()):Parcelable{

    companion object {
        /** for database */
        val TABLE_NAME = Constants.NOTE_TABLE
        val SUB_TABLE_MAIN = Constants.NOTE_MAIN
        val SUB_TABLE_TRASH = Constants.NOTE_TRASH

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

    /**
     * For parcelable
     */
    private constructor(parcelIn:Parcel) : this(
            id = parcelIn.readString(),
            uid = parcelIn.readString(),
            title = parcelIn.readString(),
            body = parcelIn.readString(),
            tags = parcelIn.readString(),
            lastEdited = parcelIn.readLong()
            )

    /**
     * writes to parcel
     * @param out Written Parcel
     * @param flags Flags
     */
    override fun writeToParcel(out: Parcel?, flags: Int) {
        out!!.writeString(id)
        out.writeString(uid)
        out.writeString(title)
        out.writeString(body)
        out.writeString(tags)
        out.writeLong(lastEdited)
    }

    override fun describeContents(): Int = 0

}