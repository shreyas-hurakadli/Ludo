package com.shreyashurakadli.engine.state.piece

import com.shreyashurakadli.engine.state.position.Position

@ConsistentCopyVisibility
data class Piece internal constructor(
    val id: Int,
    val position: Position = Position.initialPosition(),
    val status: PieceStatus = PieceStatus.InBase
) {
    companion object {
        fun initialPieces(): List<Piece> =
            listOf(
                Piece(id = 0),
                Piece(id = 1),
                Piece(id = 2),
                Piece(id = 3)
            )
    }
}