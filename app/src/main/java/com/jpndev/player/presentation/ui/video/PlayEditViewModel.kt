package com.jpndev.player.presentation.ui.video

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jpndev.player.MainActivity
import com.jpndev.player.R
import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.MUpdateData
import com.jpndev.player.data.model.PJUrl
import com.jpndev.player.data.util.Resource
import com.jpndev.player.databinding.ActivityPlayEditBinding
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.HOME_WEBURL
import com.jpndev.player.presentation.ui.IS_WEBURL
import com.jpndev.player.presentation.ui.VideosFiles
import com.jpndev.player.presentation.ui.video.CastPlayActivity
import com.jpndev.player.presentation.ui.video.PlayActivity
import com.jpndev.player.presentation.ui.video.VFolderActivity
import com.jpndev.player.presentation.ui.video.VideoPlayerActivity
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


class PlayEditViewModel (
    private val app: Application,
    public val usecase: UseCase

) : AndroidViewModel(app)
{
    private var videosFiles = ArrayList<VideosFiles>()

    private var listaVFolder = ArrayList<String>()

    val mld_Progress: MutableLiveData<Resource<VideosFiles>> = MutableLiveData()

    val mld_videofiles: MutableLiveData<ArrayList<VideosFiles>> = MutableLiveData()
    val mld_videoFolders: MutableLiveData<ArrayList<String>> = MutableLiveData()
    lateinit var activity :Activity

    fun setActiivty(temp: Activity) =viewModelScope.launch {
        activity=temp
    }


    fun showMainAcivity(temp: Activity?=activity) =viewModelScope.launch {
        val intent = Intent(temp, MainActivity::class.java)
    //    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
    }


    lateinit var simpleExoPlayer:SimpleExoPlayer
    lateinit var binding:ActivityPlayEditBinding
     fun initExoPlayer(tempbinding: ActivityPlayEditBinding) {
        simpleExoPlayer = SimpleExoPlayer.Builder(activity).setSeekForwardIncrementMs(6000)
            .setSeekBackIncrementMs(6000)
            .build()
         binding=tempbinding
         binding.playerView.player = simpleExoPlayer
         binding.playerView.keepScreenOn = true
    }
     public fun setVideoPath2(path :String?= HOME_WEBURL, isWebpath:Boolean=true) {
        if(path != null) {
            val uri=if(isWebpath) Uri.parse(path) else Uri.fromFile( File(path))
            usecase.logsource.addLog("PEVM setVideoPath uri = "+uri)
            val factory = DefaultDataSourceFactory(activity,
                Util.getUserAgent(activity, activity.getString(R.string.app_name)))
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(
                MediaItem.fromUri(uri))

            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.playWhenReady = true
        }
    }
    public fun setHomeVideo() {

            val  isWebpath=true
            val  path= HOME_WEBURL
            val uri=if(isWebpath) Uri.parse(path) else Uri.fromFile( File(path))
            usecase.logsource.addLog("PEVM setHomeVideo uri = "+uri)
            val factory = DefaultDataSourceFactory(activity,
                Util.getUserAgent(activity, activity.getString(R.string.app_name)))
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(
                MediaItem.fromUri(uri))

            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.playWhenReady = true

    }
    fun clearVideoPath() {
        binding?.let{
            binding.urlAtv.setText(HOME_WEBURL)

        }
    }
    fun saveVideoPath(path: String) {
        viewModelScope.launch {
            val newRowId = usecase.executeSavePJUrl(PJUrl(url = path))
            usecase.logsource.addLog("SavePJUrl custom= $path")
            if (newRowId > -1) {
                usecase.logsource.addLog("SavePJUrl Added Successfully $newRowId")
            } else {
                usecase.logsource.addLog("SavePJUrl Error Occurred")
            }
        }
    }
    var count=0;
    fun refreshLayout() {
        binding?.let{
            count++
            if(count%2==0)showBar() else hideBar()
            if(count==1000)
                count=0

        }
    }

    fun showBar() {
        binding?.let{
            it.barCview.visibility=View.VISIBLE

        }
    }

    fun hideBar() {
        binding?.let{
            it.barCview.visibility=View.GONE

        }
    }












    fun showPlayActivity(temp: Activity?=activity,path:String) =viewModelScope.launch {
      val intent = Intent(temp, PlayActivity::class.java)

       intent.putExtra("path", path)

      //  intent.putExtra("path", "https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4")
        intent.putExtra(IS_WEBURL, false)
        activity?.startActivity(intent)
    }

    fun showVFolderActivity(temp: Activity?=activity,item:String) =viewModelScope.launch {
        val intent = Intent(temp, VFolderActivity::class.java)
        //    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("carpetaNombre", item)
        activity?.startActivity(intent)
    }




    fun getSavedPItems(): LiveData<List<VideosFiles>> = liveData<List<VideosFiles>>{
        mld_Progress.postValue(Resource.Loading())
        return@liveData refreshLocalVideos(app)
       /* usecase.executeGetPList().collect{
            emit(it)
        }*/

    }
    fun refreshLocalVideos(context: Context=app) {
       // refreshLocalAudios()
        val tempVideosFiles = ArrayList<VideosFiles>()
        val uriExternal: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(uriExternal, projection, null, null, null, null)
        // Si en el cursor se encuenta algún vídeo entonces, lo añadiremos al Array de videosFiles.
        if(cursor != null) {
            while (cursor.moveToNext()){
                val id = cursor.getString(0)
                val path = cursor.getString(1)
                val titulo = cursor.getString(2)
                val tamano = cursor.getString(3)
                val fechaAnadido = cursor.getString(4)
                val duracion :String? = cursor.getString(5)
                val nombreVideo = cursor.getString(6)

                duracion?.let{
                // Convertimos la duración a String (Que se pueda entender).
                val stringDuracion:String = String.format("%2d:%2d", TimeUnit.MILLISECONDS.toMinutes(it.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(it.toLong()) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(it.toLong())))
                videosFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))
                Log.e("Duracion", it)
                // Comprobamos que detecte el URI del PATH.
                Log.e("Path", path )
                // Obtenemos el path de donde se encuetre el .mp4 -- /storage/sd_card/videos/nombreVideo.mp4
                val primerIndex = path.lastIndexOf("/")
                // En este obtenemos /storage/sd_card/videos
                val subString = path.substring(0, primerIndex)
                Log.e("Primer Index", subString)
                val index = subString.lastIndexOf("/")
                /* Obtenemos el nombre de la carpeta donde se encuentran los vídeos, en caso de que en el
                ArrayList de VFolder no se encuentre se añadira dicha carpeta para mostrarse por el fragment. */
                val nombreVFolder = subString.substring(index + 1, primerIndex)
                Log.e("Nombre carpeta", nombreVFolder)
                if(!listaVFolder.contains(nombreVFolder))
                    listaVFolder.add(nombreVFolder)

                // Almacenmos los datos, en el arraylist temporal.
                tempVideosFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))

                }
            }
            // Cerramos el cursos
            cursor.close()
        }
        // Retornamos el arraylist temporal de los videos.
        //return tempVideosFiles
        mld_videoFolders.value=listaVFolder
        mld_videofiles.value=tempVideosFiles
    }

   // private var audioFiles = ArrayList<VideosFiles>()

    private var listAFolder = ArrayList<String>()

   // val mld_Progress: MutableLiveData<Resource<VideosFiles>> = MutableLiveData()

    val mld_audiofiles: MutableLiveData<ArrayList<VideosFiles>> = MutableLiveData()
    val mld_audioFolders: MutableLiveData<ArrayList<String>> = MutableLiveData()



    fun refreshLocalAudios(context: Context=app) {
        val tempFiles = ArrayList<VideosFiles>()
        val uriExternal: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(uriExternal, projection, null, null, null, null)
        if(cursor != null) {
            while (cursor.moveToNext()){
                val id = cursor.getString(0)
                val path = cursor.getString(1)
                val titulo = cursor.getString(2)
                val tamano = cursor.getString(3)
                val fechaAnadido = cursor.getString(4)
                val duracion = cursor.getString(5)
                val nombreVideo = cursor.getString(6)

                duracion?.let{



                // Convertimos la duración a String (Que se pueda entender).
                val stringDuracion:String = String.format("%2d:%2d", TimeUnit.MILLISECONDS.toMinutes(duracion.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(duracion.toLong()) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(duracion.toLong())))
             //   audioFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))
                Log.e("Duracion", duracion)
                Log.e("Path", path )
                val primerIndex = path.lastIndexOf("/")
                val subString = path.substring(0, primerIndex)
                Log.e("Primer Index", subString)
                val index = subString.lastIndexOf("/")
                val nombreVFolder = subString.substring(index + 1, primerIndex)
                Log.e("Nombre carpeta", nombreVFolder)
                if(!listAFolder.contains(nombreVFolder))
                    listAFolder.add(nombreVFolder)

                tempFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))

                }
            }
            // Cerramos el cursos
            cursor.close()
        }
        mld_audioFolders.value=listAFolder
        mld_audiofiles.value=tempFiles
    }



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

    val app_update_mld: MutableLiveData<Resource<MUpdateData>> = MutableLiveData()

    fun refreshAppUpdate() = viewModelScope.launch(Dispatchers.IO) {
        app_update_mld.postValue(Resource.Loading())

        try {
            if (isNetworkAvailable(app)) {

                val apiResult = usecase.executeAppUpdate()
                app_update_mld.postValue(apiResult)
            } else {
                app_update_mld.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (e: Exception) {
            app_update_mld.postValue(Resource.Error(e.message.toString()))
        }

    }


    fun showForceUpdateDialog(obj: MUpdateData,temp: Activity?=activity) {

        temp?.let{
            val alertDialogBuilder = AlertDialog.Builder(temp)
            val title:String=obj.update_title?:temp.getString(R.string.youAreNotUpdatedTitle)
            val message:String=obj.update_message?:temp.getString(R.string.youAreNotUpdatedMessage) + " " + obj.version_name + temp.getString(R.string.youAreNotUpdatedMessage1)
            alertDialogBuilder.setTitle( Html.fromHtml(title))
            alertDialogBuilder.setMessage(
                Html.fromHtml(message)

            )

            val packagename = "com.jpndev.player"
            alertDialogBuilder.setCancelable(!obj.is_force_update)
            if(!obj.is_force_update)
                alertDialogBuilder.setNegativeButton("cancel") { dialog, id -> //getPackageName()
             /*       val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)*/
                    dialog.cancel()
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

        }
    }

    fun backFn() {
        var retunFlag:Boolean=true
        viewModelScope.launch {
           val list= usecase.executeGetPJUrlList()
            var i=list.size-1
            usecase.logsource.addLog("backFn list size  $i")
            while (i>0){
                if(list.get(i).isPopBack)
                {

                    i--;
                    usecase.logsource.addLog("backFn isPopBack  minused $i")
                }
                else
                {
                    setVideoPath2(path =list.get(i).url )
                    usecase.logsource.addLog("backFn not PopBack videoset $i")
                    retunFlag=false
                    break
                }
            }


        }
        usecase.logsource.addLog("backFn retunFlag $retunFlag")
    }


}