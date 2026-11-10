package com.shreyashurakadli.netcode

import android.content.Context
import com.shreyashurakadli.engine.GameEngine
import com.shreyashurakadli.multiplayer.MultiplayerManager
import com.shreyashurakadli.multiplayer.Player
import com.shreyashurakadli.multiplayer.Protocol
import com.shreyashurakadli.multiplayer.ReceivedMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.shreyashurakadli.multiplayer.transport.Role as MultiplayerRole

class NetCode private constructor(
    private val role: Role,
    private val multiplayerManager: MultiplayerManager,
    private val gameEngine: GameEngine
) {
    val players: StateFlow<List<Player>> = multiplayerManager.players
    val incomingMessage: ReceiveChannel<ReceivedMessage> = multiplayerManager.incomingMessage
    val playerCount: StateFlow<UInt> = multiplayerManager.playerCount

    private val _token = MutableStateFlow<String?>(null)

    /**
     * Hot flow of the token to access the game.
     * Host generates it on [startHost], guest receives it on [startGuest].
     * Null until the lobby starts.
     */
    val token: StateFlow<String?> = _token.asStateFlow()

    /**
     * Starts the lobby as host: generates the room code and begins advertising.
     *
     * @param playerCount Total players including self
     */
    fun startHost(playerCount: UInt = 2U) {
        require(role is Role.Host) {
            "Only the host can start a lobby"
        }

        val code = RandomCodeGenerator.generate()
        _token.value = code
        multiplayerManager.start(token = code, playerCount = playerCount)
    }

    /**
     * Starts the lobby as guest: searches for the room identified by [token].
     * Lobby size is not taken here; the host announces it after connect.
     *
     * @param token Token to access game
     */
    fun startGuest(token: String) {
        require(role is Role.Guest) {
            "Only a guest can join a lobby"
        }
        require(RandomCodeGenerator.check(token)) {
            "The input token is invalid"
        }

        _token.update { token }
        multiplayerManager.start(token = token)
    }

    companion object {
        /**
         * Creates an instance of [NetCode].
         */
        fun create(
            name: String,
            role: Role,
            context: Context,
            protocol: Protocol,
            scope: CoroutineScope
        ): NetCode = NetCode(
            role = role,
            multiplayerManager = MultiplayerManager.create(
                name = name,
                role = role.toTransportRole(),
                context = context,
                protocol = protocol,
                scope = scope
            ),
            gameEngine = GameEngine.create()
        )
    }
}

private fun Role.toTransportRole(): MultiplayerRole = when (this) {
    Role.Host -> MultiplayerRole.Host
    Role.Guest -> MultiplayerRole.Guest
}
