package com.jpndev.player

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.databinding.ActivityMainBinding
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.topqa.QAAdapter
import com.jpndev.player.presentation.ui.topqa.QAViewModelFactory
import com.jpndev.player.presentation.ui.topqa.TopQAViewModel
import com.jpndev.player.presentation.ui.video.VideoAdapter
import com.jpndev.player.ui.manage_log.MainVMFactory

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val REQUEST_CODE_PERMISSION = 123
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var  factory: QAViewModelFactory
    lateinit var viewModel: TopQAViewModel



    @Inject
    lateinit var  mainfactory: MainVMFactory
    lateinit var viewMainModel: MainViewModel


    @Inject
    lateinit var video_adapter: VideoAdapter

    @Inject
    lateinit var vfolder_adapter: VFolderAdapter

    @Inject
    lateinit var qa_adpater: QAAdapter
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var logSourceImpl: LogSourceImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this,factory).get(TopQAViewModel::class.java)
        viewMainModel= ViewModelProvider(this,mainfactory).get(MainViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewMainModel.activity=this@MainActivity
        requestStPermission()

        /*    val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController= navHostFragment.navController
         //   val navController = findNavController(R.id.fragmentContainerView)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_more
                )
            )
        // setupActionBarWithNavController(navController, appBarConfiguration)
           binding.navView.setupWithNavController(navController)*/


        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController= navHostFragment.navController
        binding.navView.setupWithNavController(
            navController
        )


    }
/*    @RequiresApi(Build.VERSION_CODES.Q)*/
    private fun requestStPermission() = if(ContextCompat.checkSelfPermission(applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
    } else {
        //Toast.makeText(this, "requestStPermission", Toast.LENGTH_SHORT).show()
        viewMainModel.refreshLocalVideos()
       //videosFiles = obtenerVideos(this)

    }
/*    @RequiresApi(Build.VERSION_CODES.Q)*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults?.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //  Toast.makeText(this, "Permisos Granted", Toast.LENGTH_SHORT).show()
                    viewMainModel.refreshLocalVideos()
                    /*  videosFiles = obtenerVideos(this )
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.mainFragment, VFolderFragment(listaVFolder, videosFiles))
                fragmentTransaction.commit()*/
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION
                    )
                }
            }
        }
    }

    fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }
}