package com.jpndev.player.presentation.ui.video

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpndev.player.R
import com.jpndev.player.presentation.ui.IS_WEBURL
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.VideosFiles
import java.io.File

/**
 * Adapter class for video files
 * */
class VFolderFilesAdapter(
    private val context: Context,
    private var carpetasVideosFiles: ArrayList<VideosFiles> = arrayListOf<VideosFiles>()
) : RecyclerView.Adapter<VFolderFilesAdapter.ViewHolder>() {

    /**
     * onCreateView Method
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * onBindViewHolder Method
     * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.anadirViewHolder(carpetasVideosFiles[position])
        holder.itemView.setOnClickListener {
       //     val intent = Intent(context, PlayActivity::class.java)
            val intent = Intent(context, CastPlayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //2208
            intent.putExtra("path", carpetasVideosFiles[position].path)
            addLog("VFFA setOnClickListener path = " + carpetasVideosFiles[position].path)
            //intent.putExtra("path", "http://live12.akt.hotstar-cdn.net/hls/live/2003")
           // intent.putExtra(IS_WEBURL, true)
            context.startActivity(intent)
        }
    }

    /**
     * getItemCount Method
     * @return Int, size of list
     * */
    override fun getItemCount(): Int {
        return carpetasVideosFiles.size
    }

    /**
     * Method to assign video files to Adapter
     * @param tempFiles: ArrayList of VideosFiles
     * */
    fun setList(tempFiles: ArrayList<VideosFiles>? = null) {
        tempFiles?.apply {
            carpetasVideosFiles = this
        } ?: let {
            carpetasVideosFiles = arrayListOf()
        }
        notifyDataSetChanged()
    }

    private var viewModel: VideoFolderViewModel? = null

    /**
     * Method to assign View Model
     * @param temp: MainViewModel instance
     * */
    fun setViewModel(temp: VideoFolderViewModel) {
        viewModel = temp
    }

    /**
     * Method to add Logs
     * @param log_text: String, is log text
     * */
    fun addLog(log_text: String) =
        viewModel?.usecase?.logsource?.addLog("" + log_text)

    /**
     * Method to add Logs only for studio
     * @param log_text: String, is log text
     * */
    fun addStudioLog(log_text: String) =
        viewModel?. usecase?.logsource?.addStudioLog("" + log_text)
    /**
     * inner class for video files ViewHolder
     * */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val videoMenu: ImageView = itemView.findViewById(R.id.video_menu)
        private val videoNombre: TextView = itemView.findViewById(R.id.video_nombre)
        private val duracionVideo: TextView = itemView.findViewById(R.id.duracion_video)
        fun anadirViewHolder(videosFiles: VideosFiles) {
            videoNombre.text = videosFiles.nombreVideo
            duracionVideo.text = videosFiles.duracion
            Glide.with(itemView.context).load(File(videosFiles.path)).into(thumbnail)
            //  videoMenu.setOnClickListener { borrarVideo(videosFiles) }
        }

        fun borrarVideo(videosFiles: VideosFiles) {
            var alertaDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
            alertaDialog.setTitle("ALERTA")
            alertaDialog.setMessage("Va a borrar un video, esta seguro?")
            alertaDialog.setPositiveButton(
                "Si"
            ) { _, _ ->
                val video = File(videosFiles.path)
                video.delete()
                Log.e("BORRADO", videosFiles.nombreVideo + "se ha borrado correctamente")

                MediaScannerConnection.scanFile(
                    itemView.context, arrayOf(video.toString()),
                    arrayOf(video.name), null
                )
            }
            alertaDialog.setNegativeButton(
                "No"
            ) { _, _ -> }
            val alerta: AlertDialog = alertaDialog.create()
            alerta.setCanceledOnTouchOutside(false)
            alerta.show()
        }
    }
}