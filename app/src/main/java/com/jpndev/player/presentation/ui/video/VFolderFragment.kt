package  com.jpndev.player.presentation.ui.video

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpndev.player.MainActivity
import com.jpndev.player.R
import com.jpndev.player.VFolderAdapter
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.VideosFiles
import com.jpndev.player.presentation.ui.topqa.TopQAViewModel

class VFolderFragment() : Fragment() {

    lateinit var viewModel: MainViewModel


    lateinit var itemAdapter: VFolderAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewMainModel
        itemAdapter = (activity as MainActivity).vfolder_adapter
        viewModel.addLog("VFF onCreateView  ")
        itemAdapter.viewModel = viewModel
        val view = inflater.inflate(R.layout.fragment_vfolder, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.VFolderRV)
        initRcv(recyclerView)
        /* if(listaVFolder != null && listaVFolder.size > 0 && videosFiles != null) {
             val carpetaAdapter = VFolderAdapter(view.context, this.videosFiles, this.listaVFolder)
             recyclerView.adapter = carpetaAdapter
             recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
         }*/
        viewModel.mld_videoFolders.observe(viewLifecycleOwner, {
            viewModel.addLog("VFF observe size " + it?.size)
            itemAdapter.differ.submitList(it)

        })


        return view
    }

    private fun initRcv(rcv: RecyclerView) {
        rcv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = itemAdapter
        }
        itemAdapter.differ.submitList(viewModel.mld_videoFolders.value)
    }

}