package com.example.democomponentsinandroid.service

import android.content.Intent
import android.os.Bundle
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
}