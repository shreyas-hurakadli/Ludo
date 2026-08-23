package com.shreyashurakadli.multiplayer

/**
 * Handles the multiplayer functionality.
 */
abstract class MultiplayerManager {
    /**
     * Checks if the required prerequisites such as resources are available and
     * required permissions are granted.
     */
    abstract fun checkPrerequisites()

    /**
     * Establishes room participation. Acts as a unified entry point for creating a room (Owner)
     * or joining an existing one (Guest).
     * @param code Room code
     */
    abstract fun connectToRoom(code: String?): Boolean

    /**
     * Handles departure from the session. Disbands the room if the caller is the Owner in a lobby,
     * or leaves the room/game if the caller is a Guest or a player in an active game.
     */
    abstract fun exitRoom(): Boolean

    /**
     * Sends message to other peers.
     * @param message Message to send
     */
    abstract fun sendMessage(message: String)
}