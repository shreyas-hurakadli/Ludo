package com.shreyashurakadli.multiplayer.transport

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal abstract class Transport {
    protected val discoveredEndpoints: Set<Endpoint> = emptySet()

    protected val _connectedEndpoints: MutableStateFlow<Map<Endpoint, Connection>> =
        MutableStateFlow(value = emptyMap())
    val connectedEndpoints = _connectedEndpoints.asStateFlow()

    protected var localName: String? = null
    protected var clusterToken: String? = null

    /**
     * Registers the details required for connection.
     * @param localName Name that represents local node to other peers
     * @param clusterToken Room code
     */
    fun register(localName: String, clusterToken: String) {
        this.localName = localName
        this.clusterToken = clusterToken
    }

    /**
     * Checks if the required resources are available and required permissions are granted.
     */
    abstract fun checkPrerequisites()

    /**
     * Connects to a remote peer.
     * @param peer Peer ID
     */
    abstract fun connect(peer: String)

    /**
     * Disconnects from a remote peer.
     * @param peer Peer ID
     */
    abstract fun disconnect(peer: String)

    /**
     * Sends a message to the remote peer.
     * @param peer Endpoint ID
     * @param message
     */
    abstract fun sendPayload(peer: String, message: String)

    /**
     * Closes the transport channel.
     */
    abstract fun close()
}