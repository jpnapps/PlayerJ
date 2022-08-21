package com.jpndev.player.presentation.ui.video

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.jpndev.player.R
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.VideosFiles
import com.jpndev.player.utils.ToastHandler
import com.jpndev.player.utils.constants.Common
import com.jpndev.player.utils.extensions.getAppNameReplacedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Viewmodel class for handling VideoFolder screen
 * */
class VideoFolderViewModel(
    private val app: Application,
    public val usecase: UseCase
) : AndroidViewModel(app) {
    
    private lateinit var activity: Activity
    val mld_Progress: MutableLiveData<Resource<VideosFiles>> = MutableLiveData()
    val mld_videofiles: MutableLiveData<ArrayList<VideosFiles>> = MutableLiveData()

    fun addLog(log_text: String) =
        usecase.logsource.addLog("" + log_text)

    fun addStudioLog(log_text: String) =
        usecase.logsource.addStudioLog("" + log_text)

    fun setActiivty(temp: Activity) = viewModelScope.launch {
        activity = temp
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.DISPLAY_NAME
    )
    val videoUriExternal: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    /**
     * Method to open the the email app
     */
    fun refreshLocalFolderVideos(context: Context = app, folderPath: String){
        val tempVideosFiles = ArrayList<VideosFiles>()
        val seleccion = MediaStore.Video.Media.DATA + " like?"
        val seleccionArgs = arrayOf<String>("%" + folderPath + "%")
        val cursor = context.contentResolver.query(
            videoUriExternal,
            projection,
            seleccion,
            seleccionArgs,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                val path = cursor.getString(1)
                val fileName = cursor.getString(2)
                val sizeFile = cursor.getString(3)
                val dateAdded = cursor.getString(4)
                val duration: String? = cursor.getString(5)
                val fullFileName = cursor.getString(6)
                duration?.let {
                    //converts duration into mm:ss
                    val stringDuration: String = String.format(
                        "%2d:%2d", TimeUnit.MILLISECONDS.toMinutes(it.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(it.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(it.toLong())
                        )
                    )

                    //video add to temp video files list
                    tempVideosFiles.add(
                        VideosFiles(
                            id, path, fileName, fullFileName, sizeFile,
                            dateAdded, stringDuration
                        )
                    )
                }
            }
            cursor.close()
        }
        //assign video files list to livedata
        mld_videofiles.value = tempVideosFiles
    }

}