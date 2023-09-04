package com.elthobhy.islamicstory.core.domain.model

import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable


@Parcelize
data class User(
    @get:PropertyName("avatar_user")
    @set:PropertyName("avatar_user")
    var avatarUser: String? = null,
    @get:PropertyName("email_user")
    @set:PropertyName("email_user")
    var emailUser: String? = null,
    @get:PropertyName("name_user")
    @set:PropertyName("name_user")
    var nameUser: String? = null,
    @get:PropertyName("uid_user")
    @set:PropertyName("uid_user")
    var uidUser: String? = null
) : Parcelable