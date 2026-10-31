package com.shreyashurakadli.multiplayer

import com.shreyashurakadli.multiplayer.transport.Role
import com.shreyashurakadli.multiplayer.transport.Transport

/**
 * [Transport] for testing purpose only. Not a protocol; never referenced from
 * [Protocol] or production code. Transport members that tests do not exercise
 * are inert stubs.
 */
internal class FakeTransport(
    name: String = "tester"
) : Transport(
    name = name
) {
    var startCalls: Int = 0
        private set

    var tokenAtStart: String = ""
        private set

    override fun checkPrerequisites(): PrerequisitesResult = PrerequisitesResult.Ready

    override fun start(role: Role, peerCount: UInt) {
        startCalls += 1
        tokenAtStart = token
        setPeerCount(peerCount)
    }

    override fun connect(peerId: String) = Unit

    override fun disconnect(peerId: String) = Unit

    override fun sendPayload(peerId: String, message: String) = Unit

    override fun close() = Unit

    fun currentPeerCount(): UInt = peerCount

    fun publish(message: ReceivedMessage) {
        publishIncomingMessage(message)
    }

    fun cancelMailbox() {
        cancelIncomingMessage()
    }
}
