package  com.jpndev.player

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jpndev.player.databinding.VfolderItemBinding
import com.jpndev.player.presentation.ui.MainViewModel

/**
 * Adapter class for video folders
 * */
class VFolderAdapter(private val context: Context) :
    RecyclerView.Adapter<VFolderAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.equals(newItem)
        }

    }
    val differ = AsyncListDiffer(this, callback)
    /**
     * onCreateView Method
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VfolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * getItemCount Method
     * @return Int, size of list
     * */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * onBindViewHolder Method
     * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList.get(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            viewModel?.showVFolderActivity(item = item)
        }
    }

    private var viewModel: MainViewModel? = null

    /**
     * Method to assign View Model
     * @param temp: MainViewModel instance
     * */
    fun setViewModel(temp: MainViewModel) {
        viewModel = temp
    }

    /**
     * inner class for video folder ViewHolder
     * */
    inner class ViewHolder(val binding: VfolderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.nombreVFolder.text = text
        }

    }
}