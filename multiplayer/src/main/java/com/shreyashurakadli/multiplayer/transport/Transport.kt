package com.shreyashurakadli.multiplayer.transport

import android.content.Context
import com.shreyashurakadli.multiplayer.Protocol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal abstract class Transport(
    protected val name: String,
    protected val token: String
) {
    companion object {
        fun create(
            context: Context,
            name: String,
            token: String,
            protocol: Protocol
        ): Transport =
            when (protocol) {
                Protocol.NearbyConnections -> NearbyConnections(context, name, token)
            }
    }

    protected val _peers: MutableStateFlow<List<Peer>> = MutableStateFlow(emptyList())
    val peers: StateFlow<List<Peer>> = _peers.asStateFlow()

    protected var expectedPeerCount: UInt? = null
        private set

    protected fun setExpectedPeerCount(count: UInt?) {
        expectedPeerCount = count
    }

    protected fun upsertPeer(peer: Peer) {
        _peers.value = _peers.value
            .filterNot { it.id == peer.id }
            .plus(peer)
    }

    protected fun removePeerById(peerId: String) {
        _peers.value = _peers.value.filterNot { it.id == peerId }
    }

    protected fun clearPeers() {
        _peers.value = emptyList()
    }

    abstract fun checkPrerequisites()

    /**
     * @param peerCount Expected lobby size when [role] is [Role.Host]; null for [Role.Guest]
     */
    abstract fun start(role: Role, peerCount: UInt?)

    abstract fun connect(peerId: String)

    abstract fun disconnect(peerId: String)

    abstract fun sendPayload(peerId: String, message: String)

    abstract fun close()
}
