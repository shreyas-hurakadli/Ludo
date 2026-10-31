package com.shreyashurakadli.multiplayer.transport

import android.content.Context
import com.shreyashurakadli.multiplayer.PrerequisitesResult
import com.shreyashurakadli.multiplayer.Protocol
import com.shreyashurakadli.multiplayer.ReceivedMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    private val _peers: MutableStateFlow<List<Peer>> = MutableStateFlow(emptyList())
    val peers: StateFlow<List<Peer>> = _peers.asStateFlow()

    private val _incomingMessage = Channel<ReceivedMessage>(capacity = Channel.UNLIMITED)

    /**
     * Mailbox of inbound messages, in arrival order.
     * Exactly one consumer must collect it for the transport's lifetime.
     */
    val incomingMessage: ReceiveChannel<ReceivedMessage> = _incomingMessage

    /**
     * Total player count expected on this device, **including self**.
     * Set in [start]; cleared to [DEFAULT_EXPECTED_PEER_COUNT] in [close].
     */
    protected var peerCount: UInt = DEFAULT_EXPECTED_PEER_COUNT
        private set

    protected fun setPeerCount(count: UInt) {
        peerCount = count
    }

    protected fun upsertPeer(peer: Peer) {
        _peers.update { current ->
            current.filterNot { it.id == peer.id } + peer
        }
    }

    protected fun removePeer(peerId: String) {
        _peers.update { current -> current.filterNot { it.id == peerId } }
    }

    protected fun clearPeers() {
        _peers.update { emptyList() }
    }

    protected fun publishIncomingMessage(message: ReceivedMessage) {
        _incomingMessage.trySend(message)
    }

    protected fun cancelIncomingMessage() {
        _incomingMessage.cancel()
    }

    abstract fun checkPrerequisites(): PrerequisitesResult

    /**
     * @param role Role defined in [Role]
     * @param peerCount Total players including self (2–4); resolved at [com.shreyashurakadli.multiplayer.MultiplayerManager] when omitted.
     */
    abstract fun start(role: Role, peerCount: UInt)

    abstract fun connect(peerId: String)

    abstract fun disconnect(peerId: String)

    abstract fun sendPayload(peerId: String, message: String)

    abstract fun close()
}
