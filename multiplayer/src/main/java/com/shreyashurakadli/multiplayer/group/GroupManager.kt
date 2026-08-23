package com.shreyashurakadli.multiplayer.group

import com.shreyashurakadli.multiplayer.transport.Transport

/**
 * Manages a group or room of peers.
 * @param transport
 */
internal class GroupManager(
    private val transport: Transport
) {
    /**
     * List of peers in the group
     */
    val group = transport.connectedEndpoints

    /**
     * Checks if the required resources are available and required permissions are granted.
     * @param name Name that represents local node to other peers
     * @param token Room code
     */
    fun checkPrerequisites(name: String, token: String) {
        transport.register(localName = name, clusterToken = token)
        transport.checkPrerequisites()
    }

    /**
     * Adds a peer to the group
     * @param peer Peer ID
     */
    fun addPeer(peer: String) {
        transport.connect(peer)
    }

    /**
     * Removes a peer from the group
     * @param peer Peer ID
     */
    fun removePeer(peer: String) {
        transport.disconnect(peer)
    }
}