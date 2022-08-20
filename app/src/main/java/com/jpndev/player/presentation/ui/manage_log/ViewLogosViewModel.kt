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

import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.video.PlayEditActivity

import com.jpndev.player.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

const val separator = "\n"

class ViewLogosViewModel(
    private val app: Application,
    public val usecase: UseCase

) : AndroidViewModel(app) {

    var heading: String = "INIT"
    var isUpdate: Boolean = false
    var text = MutableLiveData<String>()

    init {
        addLog("init ViewLogosViewModel ")
        text.value = "Logs"
        text.value = text.value + separator + usecase.logsource.getLogs().takeLast(100000)
            .joinToString(separator)
    }

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    fun addLog(log_text: String) =
        usecase.logsource.addLog("" + log_text)

    fun reFresh() = viewModelScope.launch {
        text.value = usecase.logsource.getLogs().takeLast(100000).joinToString(separator)
    }

    fun deleteLogs() = viewModelScope.launch {
        usecase.logsource.deleteLogs(app)
        text.value = usecase.logsource.getLogs().takeLast(100000).joinToString(separator)
    }

    fun showPManageActivity(activity: Activity) = viewModelScope.launch {
        val intent = Intent(activity, PlayEditActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
    }

    fun showLifeCycleActivity(activity: Activity) = viewModelScope.launch {
        val intent = Intent(activity, PlayEditActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
    }
}


