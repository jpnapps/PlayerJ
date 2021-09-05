package  com.jpndev.player

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpndev.player.databinding.VfolderItemBinding
import com.jpndev.player.databinding.VideoItemBinding
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.VideosFiles
import com.jpndev.player.presentation.ui.video.PlayActivity
import com.jpndev.player.presentation.ui.video.VFolderActivity
import com.jpndev.player.presentation.ui.video.VideoAdapter
import java.io.File

class VFolderAdapter(private val context: Context ) : RecyclerView.Adapter<VFolderAdapter.ViewHolder>() {
/*    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.vfolder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.carpetaNombre.text = nombreVFolder[position]
        holder.itemView.setOnClickListener{
            val intent = Intent(context, VFolderActivity::class.java)
            intent.putExtra("carpetaNombre", nombreVFolder[position])
            context.startActivity(intent)
        }
    }*/

/*    override fun getItemCount(): Int {
        return nombreVFolder.size
    }
    */
private val callback =object: DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {

        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.equals(newItem)
    }

}

    val differ= AsyncListDiffer(this,callback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = VfolderItemBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        //  return tvList.size
      viewModel?.usecase?.logsource?.addLog("VFFA getItemCount "+differ.currentList.size)
        return differ.currentList.size
    }
  /*  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= differ.currentList.get(position)
      viewModel?.usecase?.logsource?.addLog("VFFA onBindViewHolder item "+item)
        holder.bind(item)
        /*holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayActivity::class.java)
            // Pasamos el path de los videos.
            intent.putExtra("path", item.path)
            context.startActivity(intent)
        }*/
        holder.itemView.setOnClickListener{
            viewModel?.showVFolderActivity(item=item)
      /*      val intent = Intent(viewModel?.activity, VFolderActivity::class.java)
            intent.putExtra("carpetaNombre", item)
            context.startActivity(intent)*/
        }
    }

    var viewModel: MainViewModel? = null


    fun setViewModels( temp: MainViewModel) {


        viewModel=temp
    }

    inner class ViewHolder(val binding: VfolderItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(text: String) {
            binding.nombreVFolder.text=text

        }

    }



    /*  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val carpetaNombre: TextView = itemView.findViewById(R.id.nombreVFolder)
      }*/
}