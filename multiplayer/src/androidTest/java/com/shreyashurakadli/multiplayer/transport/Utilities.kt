package com.shreyashurakadli.multiplayer.transport

import android.content.Context
import com.shreyashurakadli.multiplayer.PrerequisitesResult

internal fun describePrerequisitesResult(result: PrerequisitesResult): String =
    when (result) {
        PrerequisitesResult.Ready -> "Ready"
        is PrerequisitesResult.MissingPermissions ->
            "MissingPermissions(${result.permissions.joinToString()})"
        PrerequisitesResult.BluetoothUnavailable -> "BluetoothUnavailable"
        PrerequisitesResult.BluetoothDisabled -> "BluetoothDisabled"
        PrerequisitesResult.WifiDisabled -> "WifiDisabled"
        PrerequisitesResult.LocationDisabled -> "LocationDisabled"
        PrerequisitesResult.BleNotSupported -> "BleNotSupported"
    }

internal fun nearbyConnections(context: Context): NearbyConnections =
    NearbyConnections(context = context, name = "test_name")
