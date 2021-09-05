package  com.jpndev.player.presentation.ui.manage_log
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*


import com.google.gson.JsonObject
import com.jpndev.player.MainActivity
import com.jpndev.player.data.model.PItem
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.usecase.UseCase

import com.jpndev.player.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class ViewLogosViewModel (
    private val app: Application,
    public val usecase: UseCase

    ) : AndroidViewModel(app)
    {
     /*   @Inject
        lateinit var user: User
*/
        var heading:String="INIT"

        var isUpdate:Boolean=false

       // var text:String="var init"
          //  get()=pitem

       // var text:LiveData<String>="var init"

        var text = MutableLiveData<String>()


        init {
            usecase.logsource.addLog("init jjjjjjjjjjjjjjjjj ")

            val separator = "\n"

            text.value  = usecase.logsource.getLogs().takeLast(1000).joinToString(separator)

        }
        private val statusMessage = MutableLiveData<Event<String>>()
        val message: LiveData<Event<String>>
            get() = statusMessage



        fun showPManageActivity(activity: Activity) =viewModelScope.launch {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }
        fun showLifeCycleActivity(activity: Activity) =viewModelScope.launch {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }

    }


