package com.maheshgaya.android.basicnote.model

import java.util.*

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
data class User(var id: String, var firstName: String? = null, var lastName: String? = null,
                var email: String? = null)

data class Note(var id: String, var title: String = "", var body: String = "",
                var tags: MutableList<String> = mutableListOf(""))