package com.shreyashurakadli.multiplayer.transport

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.shreyashurakadli.multiplayer.PrerequisitesResult
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Device must have runtime permissions denied for the test APK before instrumentation starts.
 *
 * Run without reinstalling (Android Studio and `connectedDebugAndroidTest` install with `adb install -g`,
 * which grants those permissions):
 * ```
 * adb shell am instrument -w \
 *   -e class com.shreyashurakadli.multiplayer.transport.NearbyConnectionsPrerequisitesMissingPermissionsInstrumentedTest \
 *   com.shreyashurakadli.multiplayer.test/androidx.test.runner.AndroidJUnitRunner
 * ```
 */
@RunWith(AndroidJUnit4::class)
class NearbyConnectionsPrerequisitesMissingPermissionsInstrumentedTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun checkPrerequisites_missingPermissions() {
        val transport = nearbyConnectionsForPrerequisitesCheck(context)
        val required = NearbyPrerequisites.requiredRuntimePermissions(Build.VERSION.SDK_INT)
        val result = transport.checkPrerequisites()

        if (result is PrerequisitesResult.MissingPermissions) {
            assertEquals(required.sorted(), result.permissions.sorted())
            return
        }

        fail("Expected MissingPermissions but was ${describePrerequisitesResult(result)}")
    }
}
