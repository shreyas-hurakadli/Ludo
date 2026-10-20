package com.shreyashurakadli.multiplayer.transport

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.shreyashurakadli.multiplayer.PrerequisitesResult
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration.Companion.milliseconds

/**
 * Two phones join over Nearby Connections: host on the first phone, guest on the second.
 * Both instruments must run at the same time.
 *
 * Manual setup on both phones: Bluetooth on, Wi-Fi on and joined to the same network,
 * location on. Verified run: both roles pass in about 2 seconds.
 *
 * 1. Build and install. `-g` grants the runtime permissions at install time, because
 *    `pm grant` is blocked on the OEMs in use:
 *    ```
 *    ./gradlew :multiplayer:assembleDebugAndroidTest
 *    adb -s <phone> install -r -t -g multiplayer/build/outputs/apk/androidTest/debug/multiplayer-debug-androidTest.apk
 *    ```
 * 2. Open a blank Activity on both phones. Location is granted "while in use" only, and
 *    Nearby discovery needs it; the instrumentation process has no UI of its own:
 *    ```
 *    adb -s <phone> shell am start -n 'com.shreyashurakadli.multiplayer.test/androidx.test.core.app.InstrumentationActivityInvoker$EmptyActivity'
 *    ```
 * 3. Run host on the first phone and guest on the second, at the same time:
 *    ```
 *    adb -s <first> shell am instrument -w \
 *      -e role host \
 *      -e class com.shreyashurakadli.multiplayer.transport.NearbyConnectionsConnectInstrumentedTest \
 *      com.shreyashurakadli.multiplayer.test/androidx.test.runner.AndroidJUnitRunner
 *
 *    adb -s <second> shell am instrument -w \
 *      -e role guest \
 *      -e class com.shreyashurakadli.multiplayer.transport.NearbyConnectionsConnectInstrumentedTest \
 *      com.shreyashurakadli.multiplayer.test/androidx.test.runner.AndroidJUnitRunner
 *    ```
 * 4. Clean up on both phones (closes the blank Activity):
 *    ```
 *    adb -s <phone> shell am force-stop com.shreyashurakadli.multiplayer.test
 *    ```
 *
 * Guest [NearbyConnections.connect] runs when discovery finds the host advertising token `TEST`.
 */
@RunWith(AndroidJUnit4::class)
class NearbyConnectionsConnectInstrumentedTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun connect_remotePeerAppears() {
        val roleArg = InstrumentationRegistry.getArguments().getString("role")
        val role = when (roleArg) {
            "host" -> Role.Host
            "guest" -> Role.Guest
            else -> {
                fail(
                    "Pass -e role host or -e role guest. " +
                        "One phone runs host, the other runs guest."
                )
                return
            }
        }

        val transport = nearbyConnectionsForPrerequisitesCheck(context)
        try {
            assertPrerequisitesReady(transport)
            transport.start(role, peerCount = 2U)
            runBlocking {
                val peers = withTimeout(PEER_WAIT_MS.milliseconds) {
                    transport.peers.first { it.isNotEmpty() }
                }
                assert(peers.isNotEmpty())
            }
        } catch (e: TimeoutCancellationException) {
            fail(
                "No peer within ${PEER_WAIT_SECONDS}s on ${roleArg}. " +
                    "The other role must be running on the second phone."
            )
        } finally {
            transport.close()
        }
    }

    private fun assertPrerequisitesReady(transport: NearbyConnections) {
        val prerequisites = transport.checkPrerequisites()
        assertEquals(
            "Expected Ready but was ${describePrerequisitesResult(prerequisites)}",
            PrerequisitesResult.Ready,
            prerequisites
        )
    }

    private companion object {
        private const val PEER_WAIT_SECONDS = 60
        private const val PEER_WAIT_MS = 1000L * PEER_WAIT_SECONDS
    }
}
