package com.shreyashurakadli.multiplayer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MessageEventHandlerTest {
    private val handler = MessageEventHandler()

    private fun event(content: String): MessageCodec? =
        handler.toEvent(ReceivedMessage(senderId = "e1", content = content))

    @Test
    fun `lobby announce decodes to its count`() {
        assertEquals(MessageCodec.LobbySize(4U), event("lobby;4"))
    }

    @Test
    fun `lobby announce encodes to the wire format`() {
        assertEquals("lobby;3", MessageCodec.LobbySize(3U).encode())
    }

    @Test
    fun `unknown message name yields no event`() {
        assertNull(event("dice;6"))
    }

    @Test
    fun `content without a delimiter yields no event`() {
        assertNull(event("lobby"))
    }

    @Test
    fun `lobby size outside two to four yields no event`() {
        assertNull(event("lobby;1"))
        assertNull(event("lobby;5"))
        assertNull(event("lobby;x"))
    }
}
