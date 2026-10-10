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

internal fun nearbyConnectionsForPrerequisitesCheck(context: Context): NearbyConnections =
    NearbyConnections(context, name = "test_name", token = "TEST")
