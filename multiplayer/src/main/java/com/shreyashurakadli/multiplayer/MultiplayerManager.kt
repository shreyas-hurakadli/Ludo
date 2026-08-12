package com.shreyashurakadli.multiplayer

/**
 * Handles the multiplayer functionality.
 */
abstract class MultiplayerManager {
    /**
     * Checks if the required prerequisites such as resources are available and
     * required permissions are granted.
     */
    abstract fun checkPrerequisites(): Boolean

    /**
     * Establishes room participation. Acts as a unified entry point for creating a room (Owner)
     * or joining an existing one (Guest).
     */
    abstract fun connectToRoom()

    /**
     * Handles departure from the session. Disbands the room if the caller is the Owner in a lobby,
     * or leaves the room/game if the caller is a Guest or a player in an active game.
     */
    abstract fun exitRoom()

    /**
     * Sends message to other peers.
     */
    abstract fun sendMessage(message: String)
}