package com.jpndev.player.presentation.ui.topqa

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.jpndev.player.data.model.QA
import com.jpndev.player.databinding.RcvItemQaBinding
import com.jpndev.player.utils.LogUtils
import com.jpndev.utillibrary.expandview.Utils
import com.jpndev.utillibrary.expandview.ViewAnimationUtils


class QAAdapter():RecyclerView.Adapter<QAAdapter.MyViewHolder>() {
/*
    private val tvList = ArrayList<QA>()

    fun setList(tvShows:List<QA>){
         tvList.clear()
         tvList.addAll(tvShows)
    }
*/

    private val callback =object:DiffUtil.ItemCallback<QA>(){
        override fun areItemsTheSame(oldItem: QA, newItem: QA): Boolean {

            return oldItem.text1==newItem.text1
        }

        override fun areContentsTheSame(oldItem: QA, newItem: QA): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = RcvItemQaBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
      //  return tvList.size

        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item= differ.currentList.get(position)
       holder.bind(item)
    }




inner class MyViewHolder(val binding: RcvItemQaBinding):
RecyclerView.ViewHolder(binding.root){

   fun bind(item:QA){
        binding.text1Txv.text = item.text1
        binding.text2Txv.text = item.answer1
        //val posterURL = "https://image.tmdb.org/t/p/w500"+tvShow.posterPath
     /*   Glide.with(binding.shuffleDimv.context)
            .load(item.urlToImage)
            .into(binding.imgDimv)*/

       binding.root.setOnClickListener{OnItemClickListener?.let{
                      it(item)
       }}

       item.answer1?.isNotBlank()?.let {
           LogUtils.LOGD("QA", "Adapter  shuffleDimv visible   ")
           binding.exrlay.visibility= View.VISIBLE

           binding.shuffleDimv.visibility = View.VISIBLE
           binding.shuffleDimv.setTag(false)
           ViewAnimationUtils.collapse(binding.exrlay)
           createRotateAnimator(binding.shuffleDimv, 180f, 0f, 0L)?.start()
           binding.shuffleDimv.setOnClickListener {
               if (binding.shuffleDimv.getTag() as Boolean) {
                   binding.shuffleDimv.setTag(false)
                   ViewAnimationUtils.collapse(binding.exrlay)
                   createRotateAnimator(binding.shuffleDimv, 180f, 0f)?.start()
               } else {
                   binding.shuffleDimv.setTag(true)
                   ViewAnimationUtils.expand(binding.exrlay)
                   createRotateAnimator(binding.shuffleDimv, 0f, 180f)?.start()
               }
               // clickListener(item, position, 1)
           }
       } ?: let {
           binding.shuffleDimv.visibility = View.INVISIBLE
           binding.exrlay.visibility= View.GONE
       }
       

   }

}

    fun createRotateAnimator(
        target: View?,
        from: Float,
        to: Float,
        duration:Long=300
    ): ObjectAnimator? {
        val animator =
            ObjectAnimator.ofFloat(target, "rotation", from, to)
        animator.duration = duration
        animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animator
    }
    private var OnItemClickListener:((QA)->Unit)?=null

    fun setOnItemClickListner(listner:(QA)->Unit){

        OnItemClickListener=listner
    }

}