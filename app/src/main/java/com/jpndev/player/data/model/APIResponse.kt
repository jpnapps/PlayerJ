package com.jpndev.player.data.model


import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("data")
    val qa_list: List<QA>,
/*    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int*/
)

data class PListResponse(
    @SerializedName("pitem")
    val pitem_list: List<PItem>,
/*    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int*/
)
