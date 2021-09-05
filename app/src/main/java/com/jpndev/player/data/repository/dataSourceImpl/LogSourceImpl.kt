package com.jpndev.player.data.repository.dataSourceImpl

import com.jpndev.player.utils.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogSourceImpl {
    private var list:ArrayList<String> = ArrayList()
     fun   addLog(text:String,tag:String="jp"){
         CoroutineScope(Dispatchers.IO).launch {
             LogUtils.LOGD("jp",""+text)
             list.add(text)
         }
    }

     fun  addLogAll(temp:ArrayList<String>){
        CoroutineScope(Dispatchers.IO).launch {
        list.addAll(temp)
        }
    }

      fun getLogs(): List<String> {
        return  list
    }
}