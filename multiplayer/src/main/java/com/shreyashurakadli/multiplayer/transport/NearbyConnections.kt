package com.shreyashurakadli.multiplayer.transport

internal class NearbyConnections(
    name: String,
    token: String
) : Transport(name, token) {
    override fun checkPrerequisites() {
        TODO("Not yet implemented")
    }

    override fun start(role: Role, peerCount: UInt?) {
        TODO("Not yet implemented")
    }

    override fun connect(peerId: String) {
        TODO("Not yet implemented")
    }

    override fun disconnect(peerId: String) {
        TODO("Not yet implemented")
    }

    override fun sendPayload(peerId: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}