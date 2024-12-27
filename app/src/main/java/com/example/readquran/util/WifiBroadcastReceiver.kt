package com.example.readquran.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class WifiBroadcastReceiver(private val onWifiConnected: () -> Unit,private val onWifiDisconnected: () -> Unit) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
        if (isWifiConnected(context)) {
            onWifiConnected()
        } else {
            Toast.makeText(context, "غير متصل بشبكة Wi-Fi", Toast.LENGTH_SHORT).show()
            onWifiDisconnected()
        }
    }
    // دالة للتحقق من الاتصال بشبكة Wi-Fi
    private fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}
