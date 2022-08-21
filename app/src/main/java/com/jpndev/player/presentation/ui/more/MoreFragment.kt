package com.jpndev.player.presentation.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceFragmentCompat
import com.jpndev.player.BuildConfig
import com.jpndev.player.MainActivity
import com.jpndev.player.databinding.FragmentMoreBinding
import com.jpndev.player.R
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.about.VersionActivity
import com.jpndev.player.presentation.ui.contact.ContactUsActivity
import com.jpndev.player.presentation.ui.manage_log.ViewLogosActivity
import com.jpndev.player.presentation.ui.video.CastPlayActivity
import com.jpndev.player.presentation.ui.video.PlayActivity
import com.jpndev.player.presentation.ui.video.PlayEditActivity
import com.jpndev.player.presentation.ui.web.WebActivity
import com.jpndev.player.ui.home.HomeViewModel
import com.jpndev.player.utils.constants.Common
import com.jpndev.player.utils.extensions.gone
import com.jpndev.player.utils.extensions.visible
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MoreFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

     var privacy_url: String ?= Common.DEFAULT_PRIVACY_POLICY_URL
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewMainModel
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (BuildConfig.isLive) {
            binding.securityCard.visibility = View.GONE
            binding.pwdManagerCard.visibility = View.GONE
            binding.liflecycleCard.visibility = View.GONE
            binding.aboutUsCard.visibility = View.GONE
            binding.viewLogos.visibility = View.GONE
        } else {
            binding.viewLogos.visibility = View.VISIBLE
            binding.liflecycleCard.visibility = View.VISIBLE
            binding.pwdManagerCard.visibility = View.VISIBLE
            binding.securityCard.visibility = View.VISIBLE
            binding.aboutUsCard.visibility = View.VISIBLE
        }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.usecase?.getAPPDataFromDB2()?.privacy_policy_url?.let {
               // viewModel.addLog("privacy_policy_url enetered = "+    it)
                privacy_url=it
            }
        }
        binding.privacyPolicyCard.setOnClickListener {
            viewModel.addLog("click privacy_policy_url : "+    privacy_url)
                val intent = Intent(activity, WebActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(Common.PRIVACY_POLICY_URL, privacy_url)
                activity?.startActivity(intent)
        }
        binding.contactUsCard.setOnClickListener {
            val intent = Intent(activity, ContactUsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }

        binding.pwdManagerCard.setOnClickListener {
            val intent = Intent(activity, PlayEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)

        }
        binding.viewLogos.setOnClickListener {
            val intent = Intent(activity, ViewLogosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)

        }
        binding.liflecycleCard.setOnClickListener {
            //throw Exception("Something wrong happen")
            Crashes.generateTestCrash()
            /*   val intent = Intent(activity, ViewLogosActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
               activity?.startActivity(intent)*/

        }
        binding.shareCard.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
        binding.aboutUsCard.setOnClickListener {
            val intent = Intent(activity, ViewLogosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }
        binding.appinfoCard.setOnClickListener {
            val intent = Intent(activity, VersionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivity(intent)
        }
        binding.rateCard.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.jpndev.player&hl=en")
            activity?.startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}