package com.shreyashurakadli.engine.state.piece

import com.shreyashurakadli.engine.state.position.Position

data class Piece(
    val id: Int,
    val position: Position,
    val status: PieceStatus
)