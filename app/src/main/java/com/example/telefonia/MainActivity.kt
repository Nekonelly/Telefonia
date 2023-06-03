package com.example.telefonia

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.telefonia.receiver.FileManager
import com.example.telefonia.receiver.MiBroadcastReceiverB
import com.example.telefonia.receiver.MiReceiverTelefonia

class MainActivity : AppCompatActivity() {
    var myBroadcastReceiver = MiBroadcastReceiverB()
    var miReceiverTelefonia = MiReceiverTelefonia()
    var txtTel: EditText? = null
    var txtMessage: EditText? = null
    var message: String? = null
    var num: String? = null
    var lblMensaje: TextView? = null
    var lblNum: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lblMensaje = findViewById(R.id.lblMensaje)
        num = ""
        txtTel = findViewById(R.id.txtNum)
        txtMessage = findViewById(R.id.txtMen)
        lblNum = findViewById(R.id.lblNumero)
        val leido = FileManager.readFromFile(applicationContext)
        if (leido!!.contains("%!%")) {
            val result = leido!!.replace("\n", "").replace("\r", "").split("%!%".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            num = result[0]
            message = result[1]
        }
        lblNum!!.text = num
        lblMensaje!!.text = message
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        filter.addAction(getString(R.string.action_broadcast))
        this.registerReceiver(myBroadcastReceiver, filter)

        //Telephony.Sms .Intents.SMS_RECEIVED_ACTION
        val intentFilterTel = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        applicationContext.registerReceiver(
            miReceiverTelefonia,
            intentFilterTel
        )
    }

    private fun enviarSMS(tel: String, msj: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(
            tel, null, msj,
            null, null
        )
        Toast.makeText(
            this, "Mensaje enviado",
            Toast.LENGTH_LONG
        ).show()
    }

    fun btnProMensaje(v: View?) {
        val cadAgregar = txtTel!!.text.toString() + "%!%" + txtMessage!!.text.toString()
        FileManager.writeToFile(cadAgregar, applicationContext)
        lblNum!!.text = txtTel!!.text
        lblMensaje!!.text = txtMessage!!.text
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myBroadcastReceiver)
    }

    fun btnMenDif(v: View?) {
        enviarSMS(txtTel!!.text.toString(), txtMessage!!.text.toString())
    }
}