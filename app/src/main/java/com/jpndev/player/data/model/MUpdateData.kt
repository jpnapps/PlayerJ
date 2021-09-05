package com.jpndev.player.data.model

import android.os.Parcelable


data class MUpdateData(
    val is_force_update: Boolean=false,
    val first_url: String?=null,
    val dummy_first_url: String?=null,
    val update_title: String?=null,
    val update_message: String?=null,
    val version_name: String?=null,
    val version_code: Int=1,
    val message: String?=null,
    val errors: Any?=null,
    val browser_url: String?=null,
    val web_url: String?=null,
    val in_app: Boolean=true,
    //val web_properties: MWeb?=null,
    val status: Boolean=true,
    //val app: MApp?=null
)
/*
@Parcelize
data class MWeb(

    val close_icon: Boolean=true,
    val refresh_icon: Boolean=true,
    val actionbar: Boolean=true
): Parcelable*/
