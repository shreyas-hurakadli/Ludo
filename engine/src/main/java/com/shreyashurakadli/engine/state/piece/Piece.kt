package com.shreyashurakadli.engine.state.piece

import com.shreyashurakadli.engine.state.position.Position

@ConsistentCopyVisibility
data class Piece internal constructor(
    val id: PieceId,
    val position: Position = Position.initialPosition(),
    val status: PieceStatus = PieceStatus.InBase
) {
    companion object {
        fun initialPieces(): List<Piece> =
            listOf(
                Piece(id = PieceId(0)),
                Piece(id = PieceId(1)),
                Piece(id = PieceId(2)),
                Piece(id = PieceId(3))
            )
    }
}