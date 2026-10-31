package com.shreyashurakadli.multiplayer

import android.content.Context
import com.shreyashurakadli.multiplayer.transport.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.StateFlow

internal const val TIMEOUT_MILLIS = 5_000L

class MultiplayerManager private constructor(
    private val room: Room
) {
    val players: StateFlow<List<Player>> = room.players

    /**
     * Mailbox of messages received from peers, in arrival order.
     * Exactly one consumer must collect it for this manager's lifetime.
     */
    val incomingMessage: ReceiveChannel<ReceivedMessage> = room.incomingMessage

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
                context = context,
                name = name,
                token = roomCode,
                role = role,
                protocol = protocol,
                scope = scope
            )

            return MultiplayerManager(room)
        }
    }

    fun checkPrerequisites(): PrerequisitesResult = room.checkPrerequisites()

    fun start(expectedPlayerCount: UInt) {
        room.enter(expectedPlayerCount)
    }

    fun sendMessage(player: Player, message: String) {
        room.communicate(player, message)
    }

    fun leaveRoom() {
        room.close()
    }
}
