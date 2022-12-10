package com.aljebrastudio.islamicstory.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListDomain(
    val name: String? = null,
    val detail: String? = null,
    val photo: Int? = null,
    val umur: String? = null,
    val umat: String? = null,
    val keyId: String,
    val recentActivity: String? = null,
): Parcelable
