package com.shreyashurakadli.multiplayer.transport

import android.Manifest
import android.os.Build
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NearbyPrerequisitesTest {
    @Test
    fun requiredRuntimePermissions_alwaysIncludesFineLocation() {
        val permissions = NearbyPrerequisites.requiredRuntimePermissions(sdkInt = 24)
        assertTrue(Manifest.permission.ACCESS_FINE_LOCATION in permissions)
    }

    @Test
    fun requiredRuntimePermissions_api31_addsBluetoothPermissions() {
        val permissions = NearbyPrerequisites.requiredRuntimePermissions(sdkInt = Build.VERSION_CODES.S)
        assertEquals(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT,
            ),
            permissions
        )
    }

    @Test
    fun requiredRuntimePermissions_api33_addsNearbyWifiDevices() {
        val permissions = NearbyPrerequisites.requiredRuntimePermissions(sdkInt = Build.VERSION_CODES.TIRAMISU)
        assertTrue(Manifest.permission.NEARBY_WIFI_DEVICES in permissions)
    }
}
