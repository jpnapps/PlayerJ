package com.jpndev.player.presentation.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.lifecycle.*
import com.jpndev.player.MainActivity
import com.jpndev.player.R
import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.MUpdateData
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.video.PlayActivity
import com.jpndev.player.presentation.ui.video.VFolderActivity
import com.jpndev.player.presentation.ui.video.VideoPlayerActivity
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class SplashViewModel (
    private val app: Application,
    public val usecase: UseCase

) : AndroidViewModel(app)
{

    lateinit var activity :Activity


    fun setActiivty(temp: Activity) =viewModelScope.launch {
        activity=temp
    }


    fun showMainAcivity(temp: Activity?=activity) =viewModelScope.launch {
        val intent = Intent(temp, MainActivity::class.java)
    //    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }

    fun showPlayActivity(temp: Activity?=activity,path:String) =viewModelScope.launch {
        val intent = Intent(temp, PlayActivity::class.java)
        //    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("path", path)
        activity?.startActivity(intent)
    }

    fun showVFolderActivity(temp: Activity?=activity,item:String) =viewModelScope.launch {
        val intent = Intent(temp, VFolderActivity::class.java)
        //    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("carpetaNombre", item)
        activity?.startActivity(intent)
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

    val app_update_mld: MutableLiveData<Resource<MUpdateData>> = MutableLiveData()

    fun refreshAppUpdate() = viewModelScope.launch(Dispatchers.IO) {
        app_update_mld.postValue(Resource.Loading())

        try {
            if (isNetworkAvailable(app)) {

                val apiResult = usecase.executeAppUpdate()
                 apiResult.data?.let {
                   // checkUpdate(it)
                    app_update_mld.postValue(Resource.Success(it))
                }


            } else {
                app_update_mld.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (e: Exception) {
            app_update_mld.postValue(Resource.ServerError("refreshAppUpdate "+e.message.toString()))
        }

    }
    public fun checkUpdate(obj: MUpdateData) {

        if(getCurrentVersionCode()>0)
        {
            if(obj.version_code>getCurrentVersionCode())
            {
                showForceUpdateDialog(obj)
            }
            else
            {
                showMainAcivity()
            }
        }else{
            showMainAcivity()

        }
    }
    private fun getCurrentVersionCode() :Int{

        var currentVersion_code: Int=-1
        val packageManager = app.packageManager
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(app.packageName, 0)
            currentVersion_code = packageInfo!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return currentVersion_code
    }
    fun showForceUpdateDialog(obj: MUpdateData,temp: Activity?=activity) {
/*        if (!isFinishing && !isDestroyed) {*/
        temp?.let{
            val alertDialogBuilder = AlertDialog.Builder(temp)
            val title:String=obj.update_title?:temp.getString(R.string.youAreNotUpdatedTitle)
            val message:String=obj.update_message?:temp.getString(R.string.youAreNotUpdatedMessage) + " " + obj.version_name + temp.getString(R.string.youAreNotUpdatedMessage1)
           //alertDialogBuilder.setTitle( Html.fromHtml(title))
            alertDialogBuilder.setTitle(     HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY))
            alertDialogBuilder.setMessage(
                //Html.fromHtml(message)
                        HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY)
            )

            val packagename = "com.jpndev.player"
            alertDialogBuilder.setCancelable(!obj.is_force_update)
            if(!obj.is_force_update)
                alertDialogBuilder.setNegativeButton("cancel") { dialog, id -> //getPackageName()
             /*       val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)*/
                    dialog.cancel()
                    showMainAcivity()
                }
            alertDialogBuilder.setPositiveButton(
                R.string.update
            ) { dialog, id -> //getPackageName()
                temp.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packagename")
                    )
                )
                dialog.cancel()
            }
            //   alertDialogBuilder.show();
            val alert = alertDialogBuilder.create()
            alert.show()
       /* }*/
        }
    }
}