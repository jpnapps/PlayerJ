package com.jpndev.player.presentation.ui.topqa

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.usecase.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TopQAViewModel (
    private val app: Application,
    private val usecase: UseCase

    ) : AndroidViewModel(app)
    {
        val top_QA_list: MutableLiveData<Resource<APIResponse>> = MutableLiveData()

        fun getTopQA( page: Int) = viewModelScope.launch(Dispatchers.IO) {
            top_QA_list.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = usecase.executeTopQA(page)
                    top_QA_list.postValue(apiResult)
                } else {
                    top_QA_list.postValue(Resource.Error("No Internet Connection"))
                }

            } catch (e: Exception) {
                top_QA_list.postValue(Resource.Error(e.message.toString()))
            }

        }


        private fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false


        }
    }