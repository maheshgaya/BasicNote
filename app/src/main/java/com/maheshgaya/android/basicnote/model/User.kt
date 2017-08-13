package com.maheshgaya.android.basicnote.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Mahesh Gaya on 8/13/17.
 */
data class User(var id: String? = null, var firstName: String? = null, var lastName: String? = null,
                var email: String? = null, var imageUrl: String? = null):Parcelable{
    
    companion object {
        @JvmField @Suppress("unused")
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {

            // This simply calls our new constructor (typically private) and
            // passes along the unmarshalled `Parcel`, and then returns the new object!
            override fun createFromParcel(parcelIn: Parcel): User {
                return User(parcelIn)
            }

            // We just need to copy this and change the type to match our class.
            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }

    private constructor(parcelIn: Parcel) : this(
            id = parcelIn.readString(),
            firstName = parcelIn.readString(),
            lastName = parcelIn.readString(),
            email = parcelIn.readString(),
            imageUrl = parcelIn.readString()
    )
    override fun writeToParcel(out: Parcel?, flags: Int) {
        out!!.writeString(id)
        out.writeString(firstName)
        out.writeString(lastName)
        out.writeString(email)
        out.writeString(imageUrl)
    }

    override fun describeContents(): Int = 0
}