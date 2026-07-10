package com.shreyashurakadli.engine.state.piece

import com.shreyashurakadli.engine.state.piece.position.Position

internal data class Piece(
    val position: Position,
    val status: Status
)