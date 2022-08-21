package com.jpndev.player.presentation.ui.contact

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.jpndev.player.R
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.utils.ToastHandler
import com.jpndev.player.utils.constants.Common
import com.jpndev.player.utils.extensions.getAppNameReplacedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactUsViewModel (
    private val app: Application,
    public val usecase: UseCase
) : AndroidViewModel(app) {
    var contact_us_email: String? = Common.DEFAULT_CONTACT_US_EMAIL
    lateinit var activity: Activity
    fun addLog(log_text: String) =
        usecase.logsource.addLog("" + log_text)

    fun addStudioLog(log_text: String) =
        usecase.logsource.addStudioLog("" + log_text)

    fun setActiivty(temp: Activity) = viewModelScope.launch {
        activity = temp
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
           usecase?.getAPPDataFromDB2()?.contact_us_email?.let {
                addLog("ContactUs enetered = " + it)
                contact_us_email = it
            }
        }
    }

    /**
     * Method to open the the email app
     */
     fun openInEmail() {

        try {
            addLog("contact_us_email click  = " + contact_us_email)
            val intent = Intent(Intent.ACTION_SENDTO)
            // only email apps should handle this
            intent.data = Uri.parse("mailto:")
            intent.putExtra(
                Intent.EXTRA_EMAIL, arrayOf(contact_us_email)
            )
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                activity.resources.getAppNameReplacedString(R.string.support_email_subject)
            )
            /*intent.putExtra(
                Intent.EXTRA_TEXT,
                helpContactUsViewModel.getDeviceInformation(this, DIGITS.THREE)
            )*/
            activity.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {

            ToastHandler.newInstance(activity)
                .mustShowToast(activity.getString(R.string.def_no_email_client_found))
        }
    }

}