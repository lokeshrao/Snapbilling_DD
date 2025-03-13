package com.snapbizz.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.snapbizz.core.R

object SnapCommonUtils {
    ///Todo
//    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
//    fun isInternetAvailable(context: Context, onNetworkAvailable: (() -> Unit)? = null): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        val network = connectivityManager.activeNetwork
//        val activeNetwork = connectivityManager.getNetworkCapabilities(network)
//        val isAvailable =
//            activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true || activeNetwork?.hasTransport(
//                NetworkCapabilities.TRANSPORT_CELLULAR
//            ) == true || activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true
//
//        if (isAvailable) {
//            onNetworkAvailable?.invoke()
//        } else {
//            try {
//                context.showMessage(context.getString(R.string.no_internet_connection_available))
//            } catch (ex: Exception) {
//                ex.message
//            }
//        }
//        return isAvailable
//    }
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) : String {
        val deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.deviceId ?: Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        }
        //hphSharedPreferences.setDeviceId(_deviceId.value)
        return deviceId
    }

}
fun Context.showMessage(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, message, length).show()
    }
}