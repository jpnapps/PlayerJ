package  com.jpndev.player.presentation.ui.video

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
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpndev.player.R
import com.jpndev.player.databinding.VideoItemBinding
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.VideosFiles
import java.io.File

class VideoAdapter(private val context: Context) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

//, private val videosFiles: ArrayList<VideosFiles>
    private val callback =object: DiffUtil.ItemCallback<VideosFiles>(){
        override fun areItemsTheSame(oldItem: VideosFiles, newItem: VideosFiles): Boolean {
            viewModel?.usecase?.logsource?.addLog("VA areItemsTheSame  ")
            return oldItem.titulo==newItem.titulo
        }

        override fun areContentsTheSame(oldItem: VideosFiles, newItem: VideosFiles): Boolean {
            viewModel?.usecase?.logsource?.addLog("VA areContentsTheSame  ")
            return oldItem.equals(newItem)
        }

    }

    val differ= AsyncListDiffer(this,callback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = VideoItemBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        //  return tvList.size

        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= differ.currentList.get(position)
        holder.bind(item)
        viewModel?.usecase?.logsource?.addLog("VA onBindViewHolder item "+item.titulo)
        holder.itemView.setOnClickListener {
            viewModel?.showPlayActivity(path=item.path)

        }
    }

    var viewModel: MainViewModel? = null


    fun setViewModels( temp: MainViewModel) {


        viewModel=temp
    }

    inner class ViewHolder(val binding: VideoItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(videosFiles: VideosFiles) {
            binding.videoNombre.text = videosFiles.nombreVideo
            Glide.with(itemView.context).load(File(videosFiles.path)).into(binding.thumbnail)
            binding.duracionVideo.text = videosFiles.duracion
           binding.videoMenu.setOnClickListener { borrarVideo(videosFiles) }
        }
        fun borrarVideo(videosFiles: VideosFiles){
            var alertaDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
            alertaDialog.setTitle("Delete")
            alertaDialog.setMessage("Are you sure to delete ?")
            alertaDialog.setPositiveButton(
                    "Si"
            ) {_, _ ->
                val video = File(videosFiles.path)
                video.delete()
                Log.e("jp", videosFiles.nombreVideo + "video deleted")

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