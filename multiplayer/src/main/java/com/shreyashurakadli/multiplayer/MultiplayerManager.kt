package com.shreyashurakadli.multiplayer

import android.content.Context
import com.shreyashurakadli.multiplayer.transport.Protocol
import com.shreyashurakadli.multiplayer.transport.Transport
import com.shreyashurakadli.multiplayer.transport.nearbyconnections.NearbyConnections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Handles the multiplayer functionality.
 */
class MultiplayerManager internal constructor(
    protocol: Protocol,
    context: Context,
    scope: CoroutineScope
) {
    private val transportManager: Transport =
        when (protocol) {
            Protocol.NearbyConnections -> NearbyConnections(context)
        }

    /**
     * All the players available in the room
     */
    val players = transportManager.connectedEndpoints
        .map { it.keys.toList() }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 1_000),
            initialValue = emptyList()
        )

    /**
     * Checks if the required prerequisites such as resources are available and
     * required permissions are granted.
     * @param name Local name
     * @param roomCode
     */
    fun checkPrerequisites(name: String, roomCode: String) {
        transportManager.checkPrerequisites()
        transportManager.register(name, roomCode)
    }

    /**
     * Establishes room participation. Acts as a unified entry point for creating a room (Owner)
     * or joining an existing one (Guest).
     * @param code Room code
     */
    fun connectToRoom(code: String?) {
        val role = when (code) {
            null -> Role.Owner
            else -> Role.Guest
        }
        transportManager.startOperation(role)
    }

    /**
     * Handles departure from the session. Disbands the room if the caller is the Owner in a lobby,
     * or leaves the room/game if the caller is a Guest or a player in an active game.
     * @param peer Peer ID
     */
    fun exitRoom(peer: String) {
        transportManager.close()
    }

    /**
     * Sends message to other peers.
     * @param peer Peer ID
     * @param message Message to send
     */
    fun sendMessage(peer: String, message: String) {
        transportManager.sendPayload(peer, message)
    }
}