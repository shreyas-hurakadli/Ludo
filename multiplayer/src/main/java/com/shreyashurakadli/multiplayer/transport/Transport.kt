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
import kotlinx.coroutines.flow.updateAndGet

internal abstract class Transport(
    protected val name: String
) {
    companion object {
        internal const val SELF_ID: String = "self"

        internal const val DEFAULT_EXPECTED_PEER_COUNT: UInt = 2U

        fun create(
            context: Context,
            name: String,
            protocol: Protocol
        ): Transport =
            when (protocol) {
                Protocol.NearbyConnections -> NearbyConnections(context = context, name = name)
            }
    }

    protected var token: String = ""
        private set

    internal fun setToken(code: String) {
        token = code
    }

    private val _peers: MutableStateFlow<List<Peer>> = MutableStateFlow(emptyList())
    val peers: StateFlow<List<Peer>> = _peers.asStateFlow()

    init {
        addSelf()
    }

    private val _incomingMessage = Channel<ReceivedMessage>(capacity = Channel.UNLIMITED)

    /**
     * Mailbox of inbound messages, in arrival order.
     * Exactly one consumer must collect it for the transport's lifetime.
     */
    val incomingMessage: ReceiveChannel<ReceivedMessage> = _incomingMessage

    protected val _peerCount: MutableStateFlow<UInt> = MutableStateFlow(DEFAULT_EXPECTED_PEER_COUNT)
    val peerCount = _peerCount.asStateFlow()

    internal fun setPeerCount(count: UInt) {
        _peerCount.update { count }
    }

    private fun addSelf() {
        _peers.update { current ->
            listOf(Peer(id = SELF_ID, displayName = name)) +
                current.filterNot { it.id == SELF_ID }
        }
    }

    protected fun addPeerIfAllowed(peer: Peer): Boolean {
        val updated = _peers.updateAndGet { current ->
            if (current.size.toUInt() < peerCount.value) {
                current.filterNot { it.id == peer.id } + peer
            } else {
                current
            }
        }
        return updated.any { it.id == peer.id }
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

    abstract fun start(role: Role, peerCount: UInt)

    abstract fun connect(peerId: String)

    abstract fun disconnect(peerId: String)

    abstract fun sendPayload(peerId: String, message: String)

    abstract fun close()
}
