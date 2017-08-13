package com.maheshgaya.android.basicnote.model

import java.net.URI

/**
 * Created by Mahesh Gaya on 8/12/17.
 */
data class User(var id: String? = null, var firstName: String? = null, var lastName: String? = null,
                var email: String? = null, var imageUrl: String? = null)

data class Note(var id: String? = null, var title: String = "", var body: String = "",
                var tags: MutableList<String> = mutableListOf(""))