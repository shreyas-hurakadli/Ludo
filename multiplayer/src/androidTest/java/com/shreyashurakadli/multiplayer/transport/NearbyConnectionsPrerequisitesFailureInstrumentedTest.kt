package com.shreyashurakadli.multiplayer.transport

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.shreyashurakadli.multiplayer.PrerequisitesResult
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Permissions are granted via [GrantPermissionRule]. Run **one test method at a time** with the
 * matching manual setup. Earlier checks in [NearbyPrerequisites.check] run first (e.g. Bluetooth
 * before Wi‑Fi).
 *
 * **Not covered:** [PrerequisitesResult.BleNotSupported], [PrerequisitesResult.BluetoothUnavailable].
 */
@RunWith(AndroidJUnit4::class)
class NearbyConnectionsPrerequisitesFailureInstrumentedTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        *NearbyPrerequisites.requiredRuntimePermissions(Build.VERSION.SDK_INT).toTypedArray()
    )

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun checkPrerequisites_bluetoothDisabled() {
        val transport = nearbyConnectionsForPrerequisitesCheck(context)

        val result = transport.checkPrerequisites()

        assertEquals(
            "Turn Bluetooth off before running this test. " +
                "Actual: ${describePrerequisitesResult(result)}",
            PrerequisitesResult.BluetoothDisabled,
            result
        )
    }

    @Test
    fun checkPrerequisites_wifiDisabled() {
        val transport = nearbyConnectionsForPrerequisitesCheck(context)

        val result = transport.checkPrerequisites()

        assertEquals(
            "Turn Wi‑Fi off before running this test (Bluetooth must be on). " +
                "Actual: ${describePrerequisitesResult(result)}",
            PrerequisitesResult.WifiDisabled,
            result
        )
    }

    @Test
    fun checkPrerequisites_locationDisabled() {
        val transport = nearbyConnectionsForPrerequisitesCheck(context)

        val result = transport.checkPrerequisites()

        assertEquals(
            "Turn location off before running this test (Bluetooth and Wi‑Fi must be on). " +
                "Actual: ${describePrerequisitesResult(result)}",
            PrerequisitesResult.LocationDisabled,
            result
        )
    }
}
