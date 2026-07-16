package com.shreyashurakadli.engine.state.piece

import com.shreyashurakadli.engine.state.position.Position

internal data class Piece(
    val id: Int,
    val position: Position,
    val status: PieceStatus
)