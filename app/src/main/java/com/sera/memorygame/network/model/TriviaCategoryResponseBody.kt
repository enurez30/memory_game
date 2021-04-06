package com.sera.memorygame.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TriviaCategoryResponseBody(
    @SerializedName("trivia_categories")
    val array: List<TriviaCategoryModel>
) : Parcelable
