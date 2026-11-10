package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Peer
import com.shreyashurakadli.multiplayer.transport.Role
import com.shreyashurakadli.multiplayer.transport.Transport
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TransportRosterTest {
    private val self = Peer(id = Transport.SELF_ID, displayName = "tester")

    private fun started(lobbySize: UInt): FakeTransport =
        FakeTransport().apply { start(role = Role.Host, peerCount = lobbySize) }

    @Test
    fun `the local player leads the roster from construction`() {
        val transport = FakeTransport()

        assertEquals(listOf(self), transport.currentPeers())
    }

    @Test
    fun `first remote peer joins a two player lobby`() {
        val transport = started(lobbySize = 2U)

        assertTrue(transport.admit(Peer(id = "e1", displayName = "Ravi")))

        assertEquals(listOf(self, Peer(id = "e1", displayName = "Ravi")), transport.currentPeers())
    }

    @Test
    fun `a third human is rejected from a two player lobby`() {
        val transport = started(lobbySize = 2U)
        transport.admit(Peer(id = "e1", displayName = "Ravi"))

        assertFalse(transport.admit(Peer(id = "e2", displayName = "Sita")))

        assertEquals(2, transport.currentPeers().size)
        assertTrue(transport.currentPeers().none { it.id == "e2" })
    }

    @Test
    fun `a freed seat admits the waiting peer`() {
        val transport = started(lobbySize = 2U)
        transport.admit(Peer(id = "e1", displayName = "Ravi"))
        transport.admit(Peer(id = "e2", displayName = "Sita"))

        transport.drop("e1")

        assertTrue(transport.admit(Peer(id = "e3", displayName = "Gita")))
        assertEquals(listOf(self, Peer(id = "e3", displayName = "Gita")), transport.currentPeers())
    }

    @Test
    fun `a bigger announce opens capacity for the rejected peer`() {
        val transport = started(lobbySize = 2U)
        transport.admit(Peer(id = "e1", displayName = "Ravi"))
        assertFalse(transport.admit(Peer(id = "e2", displayName = "Sita")))

        transport.setPeerCount(3U)

        assertTrue(transport.admit(Peer(id = "e2", displayName = "Sita")))
    }

    @Test
    fun `re-admitting a known peer replaces it without growing the roster`() {
        val transport = started(lobbySize = 3U)
        transport.admit(Peer(id = "e1", displayName = "Ravi"))

        assertTrue(transport.admit(Peer(id = "e1", displayName = "Ravi-2")))

        assertEquals(listOf(self, Peer(id = "e1", displayName = "Ravi-2")), transport.currentPeers())
    }
}
