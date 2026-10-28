package com.shreyashurakadli.netcode

import android.content.Context
import com.shreyashurakadli.engine.GameEngine
import com.shreyashurakadli.multiplayer.MultiplayerManager
import com.shreyashurakadli.multiplayer.Player
import com.shreyashurakadli.multiplayer.Protocol
import com.shreyashurakadli.multiplayer.transport.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class NetCode private constructor(
    private val multiplayerManager: MultiplayerManager,
    private val gameEngine: GameEngine
) {
    val players: StateFlow<List<Player>> = multiplayerManager.players

    companion object {
        /**
         * Creates an instance of [NetCode].
         */
        /*
         * This must be changed because :app is not going to get
         * access to role and protocol
         */
        fun create(
            name: String,
            roomCode: String,
            role: Role,
            context: Context,
            protocol: Protocol,
            scope: CoroutineScope
        ): NetCode {
            val multiplayerManager = MultiplayerManager.create(
                name = name,
                roomCode = roomCode,
                role = role,
                context = context,
                protocol = protocol,
                scope = scope
            )

            val gameEngine = GameEngine.create()

            return NetCode(
                multiplayerManager = multiplayerManager,
                gameEngine = gameEngine
            )
        }
    }
}
