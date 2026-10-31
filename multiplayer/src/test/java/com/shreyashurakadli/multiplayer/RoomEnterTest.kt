package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Role
import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.coroutines.EmptyCoroutineContext

class RoomEnterTest {
    private fun roomWith(transport: FakeTransport): Room = Room(
        transport = transport,
        role = Role.Host,
        scope = CoroutineScope(EmptyCoroutineContext)
    )

    @Test
    fun `enter binds the token before the transport starts`() {
        val transport = FakeTransport()
        val room = roomWith(transport)

        room.enter(token = "K7X2P", playerCount = 3U)

        assertEquals(1, transport.startCalls)
        assertEquals("K7X2P", transport.tokenAtStart)
        assertEquals(3U, transport.currentPeerCount())
    }

    @Test
    fun `enter defaults the lobby size to two players`() {
        val transport = FakeTransport()
        val room = roomWith(transport)

        room.enter(token = "AAAAA")

        assertEquals(2U, transport.currentPeerCount())
    }

    @Test
    fun `enter rejects a lobby size below two without starting`() {
        val transport = FakeTransport()
        val room = roomWith(transport)

        assertThrows<IllegalArgumentException> {
            room.enter(token = "AAAAA", playerCount = 1U)
        }

        assertEquals(0, transport.startCalls)
    }

    @Test
    fun `enter rejects a lobby size above four without starting`() {
        val transport = FakeTransport()
        val room = roomWith(transport)

        assertThrows<IllegalArgumentException> {
            room.enter(token = "AAAAA", playerCount = 5U)
        }

        assertEquals(0, transport.startCalls)
    }

    @Test
    fun `updateExpectedPlayerCount corrects the size after enter`() {
        val transport = FakeTransport()
        val room = roomWith(transport)
        room.enter(token = "AAAAA")

        room.updatedPlayerCount(4U)

        assertEquals(4U, transport.currentPeerCount())
    }

    @Test
    fun `updateExpectedPlayerCount rejects out of range values`() {
        val transport = FakeTransport()
        val room = roomWith(transport)
        room.enter(token = "AAAAA", playerCount = 3U)

        assertThrows<IllegalArgumentException> { room.updatedPlayerCount(1U) }
        assertThrows<IllegalArgumentException> { room.updatedPlayerCount(5U) }

        assertEquals(3U, transport.currentPeerCount())
    }
}
