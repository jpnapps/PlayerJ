package com.jpndev.player.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(
    tableName = "update_table"
)
data class MUpdateData(
    @PrimaryKey
    var id:Int=1,
    @SerializedName("force_update")
    var force_update: Boolean=false,
    @SerializedName("first_url")
    var first_url: String?=null,
    @SerializedName("dummy_first_url")
    var dummy_first_url: String?=null,
    @SerializedName("update_title")
    var update_title: String?=null,
    @SerializedName("update_message")
    var update_message: String?=null,
    @SerializedName("version_name")
    var version_name: String?=null,
    @SerializedName("version_code")
    var version_code: Int=1,
    @SerializedName("message")
    var message: String?=null,
   // val errors: Any?=null,
    @SerializedName("browser_url")
    var browser_url: String?=null,
    @SerializedName("web_url")
    var web_url: String?=null,
    @SerializedName("privacy_policy_url")
    var privacy_policy_url: String="https://github.com/jpnapps/Privacy/blob/main/PRIVACY_POLICY.md",
    @SerializedName("contact_us_email")
    var contact_us_email: String="jpapps4u@gmail.com",
    @SerializedName("in_app")
    var in_app: Boolean=true,
    //val web_properties: MWeb?=null,
    @SerializedName("status")
    var status: Boolean=true,
    //val app: MApp?=null
): Serializable
/*
@Parcelize
data class MWeb(

    val close_icon: Boolean=true,
    val refresh_icon: Boolean=true,
    val actionbar: Boolean=true
): Parcelable*/
