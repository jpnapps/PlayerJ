package  com.jpndev.player.presentation.ui.video

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.jpndev.player.MainActivity
import com.jpndev.player.R
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.VideosFiles
import javax.inject.Inject

class VideosFragment() : Fragment() {

    lateinit var viewModel: MainViewModel

    lateinit var itemAdapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).logSourceImpl.addLog("VF onCreateView")
        viewModel = (activity as MainActivity).viewMainModel
        itemAdapter = (activity as MainActivity).video_adapter
        (activity as MainActivity).logSourceImpl.addLog("VF onCreateView attached ")
        itemAdapter.viewModel = viewModel
        val view = inflater.inflate(R.layout.fragment_videos, container, false)
        val recyclerView = view.findViewById(R.id.videosRV) as RecyclerView
        initRcv(recyclerView)

        viewModel.mld_videofiles.observe(viewLifecycleOwner, {
            (activity as MainActivity).logSourceImpl.addLog("VF observe size " + it?.size)
            itemAdapter.differ.submitList(it)

        })

        /*     if( videosFiles != null || videosFiles.size > 0 ) {
                 val videoAdapter = VideoAdapter(view.context, this.videosFiles)
                 recyclerView.adapter = videoAdapter
                 recyclerView.layoutManager = LinearLayoutManager(context, VERTICAL, false)
             }*/
        return view
    }

    private fun initRcv(rcv: RecyclerView) {
        //  pitemadapter= NewsAdapter()
        rcv.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = itemAdapter
            //addOnScrollListener(this@PManageActivity.onScrollListner)
        }

    }
}