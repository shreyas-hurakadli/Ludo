package com.shreyashurakadli.multiplayer.transport

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.shreyashurakadli.multiplayer.PrerequisitesResult

internal object NearbyPrerequisites {
    fun check(context: Context): PrerequisitesResult {
        val appContext = context.applicationContext

        val missing = missingRuntimePermissions(appContext, Build.VERSION.SDK_INT)
        if (missing.isNotEmpty()) {
            return PrerequisitesResult.MissingPermissions(missing)
        }

        if (!appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return PrerequisitesResult.BleNotSupported
        }

        val bluetoothManager =
            appContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
                ?: return PrerequisitesResult.BluetoothUnavailable
        val bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            return PrerequisitesResult.BluetoothDisabled
        }

        val wifiManager =
            appContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
                ?: return PrerequisitesResult.WifiDisabled
        if (!wifiManager.isWifiEnabled) {
            return PrerequisitesResult.WifiDisabled
        }

        val locationManager =
            appContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                ?: return PrerequisitesResult.LocationDisabled
        if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
            return PrerequisitesResult.LocationDisabled
        }

        return PrerequisitesResult.Ready
    }

    internal fun requiredRuntimePermissions(sdkInt: Int): List<String> {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (sdkInt >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (sdkInt >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }
        return permissions
    }

    private fun missingRuntimePermissions(context: Context, sdkInt: Int): List<String> {
        return requiredRuntimePermissions(sdkInt).filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
    }
}
