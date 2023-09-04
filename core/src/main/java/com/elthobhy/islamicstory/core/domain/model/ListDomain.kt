package com.elthobhy.islamicstory.core.domain.model

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListDomain(
    var name: String? = null,
    var detail: String? = null,
    var umur: String? = null,
    var umat: String? = null,
    var keyId: String? = null,
    var profile: String? = null,
    var display: String? = null,
    var recentActivity: Boolean = false,
    var tag: String? = null
): Parcelable
