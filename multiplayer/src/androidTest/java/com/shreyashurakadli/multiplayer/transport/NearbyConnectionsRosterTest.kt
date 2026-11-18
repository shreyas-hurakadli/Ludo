package com.shreyashurakadli.multiplayer.transport

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * For host:
* ```
* adb shell -s <device> am instrument -w \
*   -e class com.shreyashurakadli.multiplayer.transport.NearbyConnectionsRosterTest#start_hostSendsTheRosterToAllConnections \
*   com.shreyashurakadli.multiplayer.test/androidx.test.runner.AndroidJUnitRunner
* ```
 * For guest:
 * ```
 * adb shell -s <device> am instrument -w \
 *   -e class com.shreyashurakadli.multiplayer.transport.NearbyConnectionsRosterTest#start_guestReceivesTheRoster \
 *   com.shreyashurakadli.multiplayer.test/androidx.test.runner.AndroidJUnitRunner
 * ```
*/
@RunWith(AndroidJUnit4::class)
class NearbyConnectionsRosterTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private fun waitInMinutes(duration: Duration) {
        Thread.sleep(duration.inWholeMilliseconds)
    }

    @Test
    fun start_hostSendsTheRosterToAllConnections() {
        val transport = nearbyConnections(context)

        val role = Role.Host
        val peerCount = Transport.DEFAULT_EXPECTED_PEER_COUNT

        transport.setToken("test_token")
        transport.start(role, peerCount)

        waitInMinutes(1.minutes)

        assertEquals(
            "Expected $peerCount. but was ${transport.peers.value.size}",
            peerCount.toInt(),
            transport.peers.value.size
        )
    }

    @Test
    fun start_guestReceivesTheRoster() {
        val transport = nearbyConnections(context)

        val role = Role.Guest
        val peerCount = Transport.DEFAULT_EXPECTED_PEER_COUNT

        transport.setToken("test_token")
        transport.start(role, peerCount)

        waitInMinutes(1.minutes)

        assertEquals(
            "Expected $peerCount. but was ${transport.peers.value.size}",
            peerCount.toInt(),
            transport.peers.value.size
        )
    }
}