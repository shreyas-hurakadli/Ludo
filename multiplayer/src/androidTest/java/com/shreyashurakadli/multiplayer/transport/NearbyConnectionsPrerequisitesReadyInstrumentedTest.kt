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
 * Real-device test: [NearbyConnections.checkPrerequisites] returns [PrerequisitesResult.Ready].
 *
 * **Manual setup:** Bluetooth on, Wi‑Fi on, location enabled.
 */
@RunWith(AndroidJUnit4::class)
class NearbyConnectionsPrerequisitesReadyInstrumentedTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        *NearbyPrerequisites.requiredRuntimePermissions(Build.VERSION.SDK_INT).toTypedArray()
    )

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun checkPrerequisites_returnsReadyWhenDeviceConfigured() {
        val transport = nearbyConnectionsForPrerequisitesCheck(context)

        val result = transport.checkPrerequisites()

        assertEquals(
            "Expected Ready but was ${describePrerequisitesResult(result)}",
            PrerequisitesResult.Ready,
            result
        )
    }
}
