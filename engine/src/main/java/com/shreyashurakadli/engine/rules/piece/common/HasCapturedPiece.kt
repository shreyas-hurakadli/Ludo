package com.shreyashurakadli.engine.rules.piece.common

import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.position.PositionStatus

internal class HasCapturedPiece {
    operator fun invoke(players: List<Player>, playerId: Int, piece: Piece): Boolean {
        if (piece.position.status == PositionStatus.Safe) {
            return false
        }

        for (player in players) {
            if (player.id == playerId) {
                continue
            }

            for (otherPiece in player.pieces) {
                if (otherPiece.position == piece.position) {
                    return true
                }
            }
        }

        return false
    }
}