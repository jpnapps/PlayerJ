package com.jpndev.player

import android.app.Application
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.microsoft.appcenter.AppCenter
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import javax.inject.Inject


@HiltAndroidApp
class App : Application(){
    @Inject
    lateinit var   logSource: LogSourceImpl

    suspend open fun addLog(obj: Any?) {

   /*     coroutineScope {*/

            try {


                if (obj is String) {

                    logSource.addLog("\n Text  = " +obj)


                }



            } catch (e: Exception) {
                // activity.hideProgress()
            }


        }


    override fun onCreate() {
        super.onCreate()


    }
}
