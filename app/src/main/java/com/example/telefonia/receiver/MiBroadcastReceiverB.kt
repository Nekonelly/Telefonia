package com.example.telefonia.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.telefonia.R

class MiBroadcastReceiverB : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_INPUT_METHOD_CHANGED == intent.action) {
            Toast.makeText(context, "CAMBIO DE METODO DE ENTRADA", Toast.LENGTH_LONG).show()
        } else if (intent.action == context.getString(R.string.action_broadcast)) {
            Toast.makeText(
                context, "CACHANDO MIS PROPIAS DIFUCIONES: " +
                        intent.getStringExtra("key1"), Toast.LENGTH_LONG
            ).show()
        }
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
    }

    companion object {
        private const val TAG = "MyBroadcastReceiver"
    }
}