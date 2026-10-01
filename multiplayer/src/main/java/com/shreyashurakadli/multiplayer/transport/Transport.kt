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
        /** Total players including self when the caller does not specify a lobby size. */
        internal const val DEFAULT_EXPECTED_PEER_COUNT: UInt = 2U

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

    /**
     * Total player count expected on this device, **including self**.
     * Set in [start]; cleared to [DEFAULT_EXPECTED_PEER_COUNT] in [close].
     */
    protected var expectedPeerCount: UInt = DEFAULT_EXPECTED_PEER_COUNT
        private set

    protected fun setExpectedPeerCount(count: UInt) {
        expectedPeerCount = count
    }

    protected fun upsertPeer(peer: Peer) {
        _peers.value = _peers.value
            .filterNot { it.id == peer.id }
            .plus(peer)
    }

    protected fun removePeer(peerId: String) {
        _peers.value = _peers.value.filterNot { it.id == peerId }
    }

    protected fun clearPeers() {
        _peers.value = emptyList()
    }

    abstract fun checkPrerequisites()

    /**
     * @param peerCount Total players including self (2–4); resolved at [com.shreyashurakadli.multiplayer.MultiplayerManager] when omitted.
     */
    abstract fun start(role: Role, peerCount: UInt)

    abstract fun connect(peerId: String)

    abstract fun disconnect(peerId: String)

    abstract fun sendPayload(peerId: String, message: String)

    abstract fun close()
}
