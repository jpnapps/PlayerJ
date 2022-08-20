package com.jpndev.player.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jpndev.player.presentation.ui.HOME_WEBURL
import java.io.Serializable

@Entity(
    tableName = "pitem_table"
)
data class PItem   (
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,

    @SerializedName("key1")
    var key1: String="Username",

    @SerializedName("value1")
    var value1: String="",
    @SerializedName("key2")

    var key2: String="password",
    @SerializedName("value2")
    var value2:  String="",

    @SerializedName("key3")
    var key3:  String="",
    @SerializedName("value3")
    var value3: String="",
/*

    @SerializedName("text1_color")
    var text1_color: String?,
    @SerializedName("text2_color")
    var answer1_color: String?,
    @SerializedName("web_url")
    var web_url: String?,
    @SerializedName("icon")
    var icon: String?
*/
):Serializable


@Entity(
    tableName = "url_table"
)
data class PJUrl   (
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,

    @SerializedName("url")
    var url: String= HOME_WEBURL,
    @SerializedName("isPopBack")
    var isPopBack: Boolean= false,

    ):Serializable