package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.CancellationException
import kotlin.coroutines.EmptyCoroutineContext

class RoomIncomingMessageTest {
    private fun roomWith(transport: FakeTransport): Room = Room(
        transport = transport,
        role = Role.Host,
        scope = CoroutineScope(EmptyCoroutineContext)
    )

    @Test
    fun `fresh mailbox is empty and open`() {
        val room = roomWith(FakeTransport())

        val result = room.incomingMessage.tryReceive()

        assertFalse(result.isSuccess)
        assertFalse(result.isClosed)
    }

    @Test
    fun `messages are received in arrival order with sender ids`() = runBlocking {
        val transport = FakeTransport()
        val room = roomWith(transport)

        transport.publish(ReceivedMessage(senderId = "peer-a", content = "move:1"))
        assertEquals(
            ReceivedMessage(senderId = "peer-a", content = "move:1"),
            room.incomingMessage.receive()
        )

        transport.publish(ReceivedMessage(senderId = "peer-b", content = "move:2"))
        transport.publish(ReceivedMessage(senderId = "peer-a", content = "dice:3"))

        assertEquals(
            ReceivedMessage(senderId = "peer-b", content = "move:2"),
            room.incomingMessage.receive()
        )
        assertEquals(
            ReceivedMessage(senderId = "peer-a", content = "dice:3"),
            room.incomingMessage.receive()
        )
    }

    @Test
    fun `messages published before any consumer are queued not dropped`() = runBlocking {
        val transport = FakeTransport()
        val room = roomWith(transport)

        transport.publish(ReceivedMessage(senderId = "peer-a", content = "one"))
        transport.publish(ReceivedMessage(senderId = "peer-a", content = "two"))
        transport.publish(ReceivedMessage(senderId = "peer-b", content = "three"))

        assertEquals(
            listOf("one", "two", "three"),
            List(3) { room.incomingMessage.receive().content }
        )
    }

    @Test
    fun `each message is taken by exactly one consumer`() = runBlocking {
        val transport = FakeTransport()
        val room = roomWith(transport)
        transport.publish(ReceivedMessage(senderId = "peer-a", content = "only"))

        val first = room.incomingMessage.tryReceive()
        val second = room.incomingMessage.tryReceive()

        assertEquals(ReceivedMessage(senderId = "peer-a", content = "only"), first.getOrThrow())
        assertFalse(second.isSuccess)
        assertFalse(second.isClosed)
    }

    @Test
    fun `cancel closes the mailbox and drops pending messages`() = runBlocking {
        val transport = FakeTransport()
        val room = roomWith(transport)
        transport.publish(ReceivedMessage(senderId = "peer-a", content = "pending"))

        transport.cancelMailbox()

        assertThrows<CancellationException> { room.incomingMessage.receive() }

        Unit
    }

    @Test
    fun `publish from another thread reaches the consumer`() = runBlocking {
        val transport = FakeTransport()
        val room = roomWith(transport)
        val message = ReceivedMessage(senderId = "peer-x", content = "cross-thread")

        Thread { transport.publish(message) }.apply { start() }.join()

        assertEquals(message, room.incomingMessage.receive())
    }

    @Test
    fun `mailbox holds a thousand unconsumed messages without loss`() = runBlocking {
        val transport = FakeTransport()
        val room = roomWith(transport)

        repeat(1000) {
            transport.publish(ReceivedMessage(senderId = "peer-a", content = "msg:$it"))
        }

        repeat(1000) {
            assertEquals("msg:$it", room.incomingMessage.receive().content)
        }
    }
}
