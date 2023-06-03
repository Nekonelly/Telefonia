package com.example.telefonia.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast

class MiReceiverTelefonia : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sb = StringBuilder()
        sb.append(
            """
                Action: ${intent.action}
                
                """.trimIndent()
        )
        sb.append(
            """
                URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}
                
                """.trimIndent()
        )
        val log = sb.toString()
        Log.d(TAG, log)
        Toast.makeText(context, log, Toast.LENGTH_LONG).show()
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Toast.makeText(context, "Me llego un mensaje", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "MiReceiverTelefonia"
    }
}

