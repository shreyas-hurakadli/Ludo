package com.shreyashurakadli.engine.state.piece

import com.shreyashurakadli.engine.state.position.Position

@ConsistentCopyVisibility
data class Piece internal constructor(
    val id: Int,
    val position: Position,
    val status: PieceStatus
)