package com.jpndev.player.presentation.ui.topqa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpndev.player.MainActivity
import com.jpndev.player.R
import com.jpndev.player.data.util.Resource

import com.jpndev.player.databinding.FragmentTopQaBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TopQAFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopQAFragment : Fragment() {

    lateinit var viewModel: TopQAViewModel
    lateinit var binding: FragmentTopQaBinding

    lateinit var itemadapter: QAAdapter
    var country:String="US"
    var page :Int=22
    var pages:Int=0
    var isLoading =false
    var isScrolling=false
    var isLastPage=true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_qa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentTopQaBinding.bind(view)
        viewModel=(activity as MainActivity).viewModel
        itemadapter=(activity as MainActivity).qa_adpater
        itemadapter.setOnItemClickListner {
          /*  val bundle=Bundle().apply {
                putSerializable("selected_item",it)
            }
            findNavController().navigate(R.id.action_newsFragment_to_infoFragment,bundle)*/
        }
        initRcv()
        viewQaList()



    }




    private fun viewQaList() {
        viewModel.getTopQA(page)
        viewModel.top_QA_list.observe(viewLifecycleOwner,{response->
            when(response)
            {
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {
                        itemadapter.differ.submitList(it.qa_list.toList())
                       /* if(it.totalResults%20==0)
                        {
                            pages=it.totalResults/20
                        }
                        else
                        {
                            pages=it.totalResults/20+1
                        }
                        isLastPage=pages==page*/
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let{
                        Toast.makeText(activity,"Error : "+it,Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }


    private fun initRcv() {
      //  itemadapter= QAAdapter()
        binding.rcv.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter=itemadapter
            addOnScrollListener(this@TopQAFragment.onScrollListner)
        }

    }

    private fun hideProgressBar() {
        isLoading=false
        binding.progressBar.visibility=View.GONE
    }

    private fun showProgressBar() {
        isLoading=true
        binding.progressBar.visibility=View.VISIBLE
    }

    private val onScrollListner = object :RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lm=binding.rcv.layoutManager as LinearLayoutManager
             val size_current_list=   lm.itemCount
             val visible_items=lm.childCount
             val toppos= lm.findFirstVisibleItemPosition()
             val hasReachedtoEnd=toppos+visible_items>=size_current_list

              if(!isLastPage && hasReachedtoEnd && isScrolling && !isLoading)
              {
                  page++
                  viewModel.getTopQA(page)
                  isScrolling=false
              }
         }

    }
}

