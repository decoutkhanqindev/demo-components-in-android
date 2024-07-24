package com.example.democomponentsinandroid.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import kotlin.random.Random

class MyBoundService : Service() {

    inner class LocalBinder : Binder() {
        fun getService() = this@MyBoundService
    }

    private val localBinder = LocalBinder()

    // su dung bound service thi tra ve doi truong IBinder
    override fun onBind(intent: Intent?): LocalBinder {
        logMessage("onBind: $intent")
        return localBinder
    }

    // connection 1 -> 0
    override fun onUnbind(intent: Intent?): Boolean {
        logMessage("onUnBind")
        return true // onRebind will be called
    }

    // connection 0 -> 1
    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        logMessage("onRebind")
    }

    override fun onCreate() {
        super.onCreate()
        logMessage("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        logMessage("onDestroy")
    }

    private fun logMessage(message: String) = Log.d(TAG, message)

    fun getData(): Int {
        logMessage("getData")
        return Random.nextInt()
    }

    companion object {
        const val TAG = "MyBoundService"
    }
}