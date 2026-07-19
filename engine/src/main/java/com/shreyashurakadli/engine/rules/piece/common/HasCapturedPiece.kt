package com.shreyashurakadli.engine.rules.piece.common

import com.shreyashurakadli.engine.rules.position.converter.RelativeToAbsolutePositionConverter
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.position.PositionStatus

internal class HasCapturedPiece(
    private val converter: RelativeToAbsolutePositionConverter
) {
    operator fun invoke(
        players: List<Player>,
        playerId: Int,
        piece: Piece,
        partCount: Int
    ): Boolean {
        if (piece.position.status == PositionStatus.Safe) {
            return false
        }

        val capturer = players.find { it.id == playerId } ?: return false

        val absolutePosition = converter.calculateAbsolutePosition(
            position = piece.position,
            partCount = partCount,
            playerPart = capturer.id
        )

        for (player in players) {
            if (player.id == playerId) {
                continue
            }

            for (otherPiece in player.pieces) {
                val otherPieceAbsolutePosition = converter.calculateAbsolutePosition(
                    position = otherPiece.position,
                    partCount = partCount,
                    playerPart = player.id
                )
                if (otherPieceAbsolutePosition == absolutePosition) {
                    return true
                }
            }

        }
        return false
    }
}