package com.example.democomponentsinandroid.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.democomponentsinandroid.R
import com.example.democomponentsinandroid.databinding.ActivityDemoServiceBinding

class DemoServiceActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDemoServiceBinding.inflate(layoutInflater)
    }

    private var bound = false
    private var myBoundServiceRef: MyBoundService? = null
    // Để bind tới một service từ client
    private val connection = object : ServiceConnection {
        // dc goi khi service dc bind thanh cong, IBinder de lay ra service  object - client
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bound = true
            myBoundServiceRef = (service as MyBoundService.LocalBinder).getService()
            // thuc hien logic
            val data = myBoundServiceRef!!.getData()
            Toast.makeText(this@DemoServiceActivity, "Connected: data=$data", Toast.LENGTH_SHORT).show()
        }

        // khi service bi crash hoac bi kill boi system -> ham se k dc goi neu client unbind service
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            myBoundServiceRef = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            // start services
            ContextCompat.startForegroundService(
                this,
                Intent(this, MyForegroundService::class.java).apply {
                    putExtra(MyForegroundService.ACTION_EXTRA_KEY, "START")
                }
            )
        }

        binding.buttonStop.setOnClickListener {
//            // start services
//            ContextCompat.startForegroundService(
//                this,
//                Intent(this, MyForegroundService::class.java).apply {
//                    putExtra(MyForegroundService.ACTION_EXTRA_KEY, "STOP")
//                }
//            )

            // stop service
            this.stopService(Intent(this, MyForegroundService::class.java).apply {
                putExtra(MyForegroundService.ACTION_EXTRA_KEY, "STOP")
            })
        }
    }

    override fun onStart() {
        super.onStart()
        this.bindService(
            Intent(this, MyBoundService::class.java),
            connection,
            Service.BIND_AUTO_CREATE // khi connection 1 -> 0 = destroy
        )
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            this.unbindService(connection)
            bound = false
        }
    }
}