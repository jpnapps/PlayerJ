package com.jpndev.player.data.util

import com.jpndev.player.R

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val color: Int = R.color.cursor1_color,
    val isShow: Boolean = false
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class HideLoading<T>(data: T? = null) : Resource<T>(data)
    class ShowAlert<T>(message: String,color: Int= R.color.cursor1_color,isShow: Boolean=false, data: T? = null) : Resource<T>(data, message,color,isShow)
    class ServerError<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

