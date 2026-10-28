package com.shreyashurakadli.netcode

import android.content.Context
import com.shreyashurakadli.engine.GameEngine
import com.shreyashurakadli.multiplayer.MultiplayerManager
import com.shreyashurakadli.multiplayer.Player
import com.shreyashurakadli.multiplayer.Protocol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import com.shreyashurakadli.multiplayer.transport.Role as MultiplayerRole

class NetCode private constructor(
    private val multiplayerManager: MultiplayerManager,
    private val gameEngine: GameEngine
) {
    val players: StateFlow<List<Player>> = multiplayerManager.players

    companion object {
        /**
         * Creates an instance of [NetCode].
         */
        fun create(
            name: String,
            roomCode: String,
            role: Role,
            context: Context,
            protocol: Protocol,
            scope: CoroutineScope
        ): NetCode = when (role) {
            Role.Host -> host(
                name = name,
                roomCode = roomCode,
                context = context,
                protocol = protocol,
                scope = scope
            )
            Role.Guest -> guest(
                name = name,
                roomCode = roomCode,
                context = context,
                protocol = protocol,
                scope = scope
            )
        }

        private fun host(
            name: String,
            roomCode: String,
            context: Context,
            protocol: Protocol,
            scope: CoroutineScope
        ): NetCode {
            val multiplayerManager = MultiplayerManager.create(
                name = name,
                roomCode = roomCode,
                role = MultiplayerRole.Host,
                context = context,
                protocol = protocol,
                scope = scope
            )

            return NetCode(
                multiplayerManager = multiplayerManager,
                gameEngine = GameEngine.create()
            )
        }

        private fun guest(
            name: String,
            roomCode: String,
            context: Context,
            protocol: Protocol,
            scope: CoroutineScope
        ): NetCode {
            val multiplayerManager = MultiplayerManager.create(
                name = name,
                roomCode = roomCode,
                role = MultiplayerRole.Guest,
                context = context,
                protocol = protocol,
                scope = scope
            )

            return NetCode(
                multiplayerManager = multiplayerManager,
                gameEngine = GameEngine.create()
            )
        }
    }
}
