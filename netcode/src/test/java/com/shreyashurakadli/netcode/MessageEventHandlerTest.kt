package com.shreyashurakadli.netcode

import com.shreyashurakadli.multiplayer.ReceivedMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MessageEventHandlerTest {
    private val handler = MessageEventHandler()

    @Test
    fun `toEvent returns LobbySize for a valid lobby message`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;3")

        val event = handler.toEvent(message)

        assertEquals(MessageCodec.LobbySize(3U), event)
    }

    @Test
    fun `toEvent returns LobbySize for count of two`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;2")

        val event = handler.toEvent(message)

        assertEquals(MessageCodec.LobbySize(2U), event)
    }

    @Test
    fun `toEvent returns LobbySize for count of four`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;4")

        val event = handler.toEvent(message)

        assertEquals(MessageCodec.LobbySize(4U), event)
    }

    @Test
    fun `toEvent returns null for a count below two`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;1")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent returns null for a count above four`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;5")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent returns null for a message without the delimiter`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent returns null for an unknown message name`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "unknown;3")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent returns null for non-numeric payload`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;abc")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent returns null for an empty payload`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent returns null for negative values`() {
        val message = ReceivedMessage(senderId = "peer-1", content = "lobby;-1")

        val event = handler.toEvent(message)

        assertNull(event)
    }

    @Test
    fun `toEvent preserves senderId in the decoded event`() {
        val message = ReceivedMessage(senderId = "peer-abc", content = "lobby;3")

        val event = handler.toEvent(message)

        assertEquals(MessageCodec.LobbySize(3U), event)
        assertEquals("peer-abc", message.senderId)
    }
}