package com.shreyashurakadli.multiplayer.transport

import com.shreyashurakadli.multiplayer.Protocol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal abstract class Transport(
    protected val name: String,
    protected val token: String
) {
    companion object {
        fun create(name: String, token: String, protocol: Protocol): Transport =
            when (protocol) {
                Protocol.NearbyConnections -> NearbyConnections(name, token)
            }
    }

    protected val _peers: MutableStateFlow<List<Peer>> = MutableStateFlow(emptyList())
    val peers: StateFlow<List<Peer>> = _peers.asStateFlow()

    abstract fun checkPrerequisites()

    /**
     * @param peerCount Expected lobby size when [role] is [Role.Host]; null for guest or unknown.
     */
    abstract fun start(role: Role, peerCount: UInt?)

    abstract fun connect(peerId: String)

    abstract fun disconnect(peerId: String)

    abstract fun sendPayload(peerId: String, message: String)

    abstract fun close()
}
