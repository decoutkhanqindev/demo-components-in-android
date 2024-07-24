package com.example.democomponentsinandroid.broadcastreceiver

import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.democomponentsinandroid.R
import com.example.democomponentsinandroid.databinding.ActivityDemoBroadCastReceiverBinding
import com.example.democomponentsinandroid.databinding.ActivityDemoServiceBinding

class DemoBroadCastReceiver : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDemoBroadCastReceiverBinding.inflate(layoutInflater)
    }

    private val activityLauncherPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.requestReceiveSMS.setOnClickListener {
            activityLauncherPermissions.launch(android.Manifest.permission.RECEIVE_SMS)
        }

        val smsBroadCastReceiver = SMSBroadCastReceiver()
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsBroadCastReceiver, intentFilter)
    }

    override fun onStop() {
        unregisterReceiver(SMSBroadCastReceiver())
        super.onStop()
    }
}