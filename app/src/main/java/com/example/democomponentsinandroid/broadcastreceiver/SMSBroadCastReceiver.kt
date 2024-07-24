package com.example.democomponentsinandroid.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import com.example.democomponentsinandroid.lifecycle.SecondActivity

class SMSBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle: Bundle? = intent?.extras
        bundle.let {
            val extractMsg: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            extractMsg.forEach { smsMessage ->
                val messageBody: String = smsMessage.messageBody
                val messageAddress: String? = smsMessage.originatingAddress

                val intentStart = Intent(context, SecondActivity::class.java).apply {
                    putExtra("messageBody", messageBody)
                    putExtra("messageAddress", messageAddress)
                    flags = FLAG_ACTIVITY_NEW_TASK
                }
                context?.startActivity(intentStart)
            }
        }
    }
}