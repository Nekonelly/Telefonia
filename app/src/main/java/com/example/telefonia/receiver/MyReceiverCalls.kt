package com.example.telefonia.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class MyReceiverCalls : BroadcastReceiver() {
    var num: String? = null
    var message: String? = null
    var entrada: String? = null
    override fun onReceive(context: Context, intent: Intent) {
        num = ""
        message = ""
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                super.onCallStateChanged(state, incomingNumber)
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    numEntrante = incomingNumber.substring(3)
                    incomingCall = true
                }
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (incomingCall) {
                        incomingCall = false
                        entrada = FileManager.readFromFile(context)!!
                            .replace("\n", "").replace("\r", "")
                        if (entrada!!.contains("%!%")) {
                            num = entrada!!.split("%!%".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[0]
                            message =
                                entrada!!.split("%!%".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1]
                        }
                        Log.d("HOLA", "onCallStateChanged: $num")
                        Log.d("HOLA", "onCallStateChanged: $incomingNumber")
                        num = num!!.replace(" ", "")
                        Toast.makeText(
                            context,
                            "Registrado: $num\nEntrante: $incomingNumber",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (num == incomingNumber) {
                            enviarSMS(incomingNumber, message!!, context)
                        }
                    }
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun enviarSMS(tel: String, msj: String, context: Context) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(tel, null, msj, null, null)
        Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_SHORT).show()
    }

    companion object {
        var incomingCall = false
        var numEntrante: String? = null
    }
}