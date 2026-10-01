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

    fun enter(playerCount: UInt?) {
        when (role) {
            Role.Host -> handleHostEntry(playerCount)
            Role.Guest -> handleGuestEntry(playerCount)
        }
    }

    private fun handleHostEntry(playerCount: UInt?) {
        val count = requireNotNull(playerCount) {
            "Host must set expected player count"
        }
        require(count in 2U..4U) {
            "Player count must be between 2 and 4"
        }
        transport.start(role = role, peerCount = count)
    }

    private fun handleGuestEntry(playerCount: UInt?) {
        require(playerCount == null) {
            "Guest must not set player count"
        }
        transport.start(role = role, peerCount = null)
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
