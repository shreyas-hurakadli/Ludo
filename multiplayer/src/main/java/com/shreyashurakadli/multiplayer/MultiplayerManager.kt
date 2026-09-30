package com.shreyashurakadli.multiplayer

import android.content.Context
import com.shreyashurakadli.multiplayer.transport.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class MultiplayerManager private constructor(
    private val room: Room,
    context: Context
) {
    val players: StateFlow<List<Player>> = room.players

    companion object {
        fun create(
            name: String,
            roomCode: String,
            role: Role,
            context: Context,
            protocol: Protocol,
            scope: CoroutineScope
        ): MultiplayerManager {
            val room = Room.create(
                name = name,
                token = roomCode,
                role = role,
                protocol = protocol,
                scope = scope
            )

            return MultiplayerManager(room, context)
        }
    }

    fun checkPrerequisites() {
        room.checkPrerequisites()
    }

    fun start(playerCount: UInt?) {
        room.enter(playerCount)
    }

    fun sendMessage(player: Player, message: String) {
        room.communicate(player, message)
    }

    fun leaveRoom() {
        room.close()
    }
}
