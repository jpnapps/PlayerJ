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
import com.jpndev.player.presentation.ui.VideosFiles
import java.io.File

class VistaVFolderAdapter(private val context: Context, private val carpetasVideosFiles: ArrayList<VideosFiles>) : RecyclerView.Adapter<VistaVFolderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.anadirViewHolder(carpetasVideosFiles[position])
        holder.itemView.setOnClickListener {
           // val intent = Intent(context, VideoPlayerActivity::class.java)
            val intent = Intent(context, CastPlayActivity::class.java)
            intent.putExtra("path", carpetasVideosFiles[position].path)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return carpetasVideosFiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val videoMenu: ImageView = itemView.findViewById(R.id.video_menu)
        private val videoNombre: TextView = itemView.findViewById(R.id.video_nombre)
        private val duracionVideo: TextView = itemView.findViewById(R.id.duracion_video)
        fun anadirViewHolder(videosFiles: VideosFiles) {
            videoNombre.text = videosFiles.nombreVideo
            duracionVideo.text = videosFiles.duracion
            Glide.with(itemView.context).load(File(videosFiles.path)).into(thumbnail)
            videoMenu.setOnClickListener { borrarVideo(videosFiles) }
        }
        fun borrarVideo(videosFiles: VideosFiles){
            var alertaDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
            alertaDialog.setTitle("ALERTA")
            alertaDialog.setMessage("Va a borrar un video, esta seguro?")
            alertaDialog.setPositiveButton(
                    "Si"
            ) {_, _ ->
                val video = File(videosFiles.path)
                video.delete()
                Log.e("BORRADO", videosFiles.nombreVideo + "se ha borrado correctamente")

                MediaScannerConnection.scanFile(itemView.context, arrayOf(video.toString()),
                        arrayOf(video.name), null)
            }
            alertaDialog.setNegativeButton(
                    "No"
            ) {_, _ -> }
            val alerta: AlertDialog = alertaDialog.create()
            alerta.setCanceledOnTouchOutside(false)
            alerta.show()
        }
    }
}