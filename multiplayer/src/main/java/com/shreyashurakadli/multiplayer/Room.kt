package com.shreyashurakadli.multiplayer

import android.content.Context
import com.shreyashurakadli.multiplayer.transport.Role
import com.shreyashurakadli.multiplayer.transport.Transport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job

internal class Room(
    private val transport: Transport,
    private val role: Role,
    private val scope: CoroutineScope
) {
    companion object {
        internal const val DEFAULT_EXPECTED_PLAYER_COUNT = 2U
        private const val MIN_ROOM_SIZE = 2U
        private const val MAX_ROOM_SIZE = 4U

        fun create(
            context: Context,
            name: String,
            token: String,
            role: Role,
            protocol: Protocol,
            scope: CoroutineScope
        ): Room {
            val transport = Transport.create(
                context = context,
                name = name,
                token = token,
                protocol = protocol
            )
            val roomScope = CoroutineScope(
                scope.coroutineContext + SupervisorJob(scope.coroutineContext.job)
            )

            return Room(transport, role, roomScope)
        }
    }

    val players: StateFlow<List<Player>> =
        transport.peers
            .map { peerList ->
                peerList.map { peer -> peer.toPlayer() }
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun enter(expectedPlayerCount: UInt) {
        require(expectedPlayerCount in MIN_ROOM_SIZE..MAX_ROOM_SIZE) {
            "Expected player count must be between $MIN_ROOM_SIZE and $MAX_ROOM_SIZE"
        }
        transport.start(role = role, peerCount = expectedPlayerCount)
    }

    fun checkPrerequisites() {
        transport.checkPrerequisites()
    }

    fun communicate(player: Player, message: String) {
        transport.sendPayload(player.id, message)
    }

    fun close() {
        scope.cancel()
        transport.close()
    }
}
