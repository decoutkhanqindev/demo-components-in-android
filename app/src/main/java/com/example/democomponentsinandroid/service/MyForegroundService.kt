package com.example.democomponentsinandroid.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.democomponentsinandroid.R


class MyForegroundService : Service() {
    // return null thi k su dung bound service
    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        logMessage("onCreate")
    }

    // nhan thong bao tu ben ngoai
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logMessage("onStartCommand: $intent")
        createNotificationChannel()
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification").setContentText("My Foreground Service...")
                .setPriority(NotificationCompat.PRIORITY_HIGH).build()
        // start foreground service
        startForeground(NOTIFICATION_ID, notification)

        val isMainThread = Looper.myLooper() == Looper.getMainLooper()
        logMessage("Is main thread: $isMainThread")

        val action = intent?.getStringExtra(ACTION_EXTRA_KEY)
        logMessage("Action: $action")
        when (action) {
            null -> {
            }

            "START" -> {
            }

            "RESUME" -> {
            }

            "PAUSE" -> {
            }

            "STOP" -> {
//                 stopSelf() // tu kill chinh no
            }

            else -> {
            }
        }

        return START_STICKY
        // neu service bi he thong kill thi se dc restart lai
        // nhung khong gui lai intent cuoi cung ma gui lai intent null
        // neu co intent dang cho thi se dc nhay vao

        // START_NOT_STICKY:
        // neu service bi he thong khi bi kill se k tao lai service tru khi co pending intent (intent gui data)
        // tranh viec tao lai service khong can thiet, nhung neu co intent chua dc hoan thanh thi se tiep tuc

        // START_REDELIVER_INTENT: vd nhu tai file
        // neu service bi he thong khi bi kill se tao lai service voi intent cuoi cung
        // neu nhu co nhung intent dang cho thuc hien thi lan luot nhay vao
    }

    override fun onDestroy() {
        super.onDestroy()
        logMessage("onDestroy")
    }

    private fun Context.createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun logMessage(message: String) = Log.d(TAG, message)

    companion object {
        const val TAG = "MyForegroundService"
        const val CHANNEL_ID = "MyForegroundServiceChannel"
        const val NOTIFICATION_ID = 1
        const val ACTION_EXTRA_KEY = "action_extra_key"
    }
}