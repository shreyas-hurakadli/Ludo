package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Role
import com.shreyashurakadli.multiplayer.transport.Transport

/**
 * Hand-written [Transport] for tests. Not a protocol; never referenced from
 * [Protocol] or production code. Transport members that tests do not exercise
 * are inert stubs.
 */
internal class FakeTransport(
    name: String = "tester",
    token: String = "ROOM1"
) : Transport(
    name = name,
    token = token
) {
    override fun checkPrerequisites(): PrerequisitesResult = PrerequisitesResult.Ready

    override fun start(role: Role, peerCount: UInt) = Unit

    override fun connect(peerId: String) = Unit

    override fun disconnect(peerId: String) = Unit

    override fun sendPayload(peerId: String, message: String) = Unit

    override fun close() = Unit

    fun publish(message: ReceivedMessage) {
        publishIncomingMessage(message)
    }

    fun cancelMailbox() {
        cancelIncomingMessage()
    }
}
