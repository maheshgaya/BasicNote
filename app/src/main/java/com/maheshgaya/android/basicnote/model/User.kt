package com.maheshgaya.android.basicnote.model

import android.os.Parcel
import android.os.Parcelable
import com.maheshgaya.android.basicnote.Constants

/**
 * Created by Mahesh Gaya on 8/13/17.
 */
/**
 * User model
 * @param id User ID from Firebase
 * @param firstName First name of the user
 * @param lastName Last name of the user
 * @param email Email of the user
 * @param imageUrl ImageUrl of the user's profile picture
 */
data class User(var id: String? = null, var firstName: String? = null, var lastName: String? = null,
                var email: String? = null, var imageUrl: String? = null):Parcelable{
    
    companion object {
        /** for database */
        val TABLE_NAME = Constants.USER_TABLE

        val COLUMN_ID = "id"
        val COLUMN_FIRST_NAME = "firstName"
        val COLUMN_LAST_NAME = "lastName"
        val COLUMN_EMAIL = "email"
        val COLUMN_IMAGE_URL = "imageUrl"


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