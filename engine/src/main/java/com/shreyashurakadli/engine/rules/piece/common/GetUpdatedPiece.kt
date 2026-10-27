package com.shreyashurakadli.engine.rules.piece.common

import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.player.Player

internal class GetUpdatedPiece {
    operator fun invoke(player: Player, pieceId: PieceId): Piece =
        player.pieces.find { it.id == pieceId }
            ?: throw IllegalStateException("Piece not found")
}
