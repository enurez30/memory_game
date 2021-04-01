package com.sera.memorygame.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TriviaResponseBody(
    @SerializedName("response_code")
    val code: String,
    @SerializedName("results")
    val result: List<TriviaObjectModel>
) : Parcelable
