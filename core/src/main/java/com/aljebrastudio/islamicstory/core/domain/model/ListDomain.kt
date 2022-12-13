package com.aljebrastudio.islamicstory.core.domain.model

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class ListDomain(
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String? = null,
    @get:PropertyName("detail")
    @set:PropertyName("detail")
    var detail: String? = null,
    @get:PropertyName("umur")
    @set:PropertyName("umur")
    var umur: String? = null,
    @get:PropertyName("umat")
    @set:PropertyName("umat")
    var umat: String? = null,
    @get:PropertyName("keyId")
    @set:PropertyName("keyId")
    var keyId: String? = null,
    @get:PropertyName("profile")
    @set:PropertyName("profile")
    var profile: String? = null,
    @get:PropertyName("display")
    @set:PropertyName("display")
    var display: String? = null,
    @get:PropertyName("recentActivity")
    @set:PropertyName("recentActivity")
    var recentActivity: String? = null,
): Parcelable
