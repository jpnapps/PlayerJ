package com.jpndev.player.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "article_table"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    @SerializedName("text1")
    val text1: String?,
    @SerializedName("text2")
    val answer1: String?,
    @SerializedName("text1_color")
    val text1_color: String?,
    @SerializedName("text2_color")
    val answer1_color: String?,
    @SerializedName("web_url")
    val web_url: String?,
    @SerializedName("icon")
    val icon: String?
):Serializable