package com.shreyashurakadli.engine.state.player

import com.shreyashurakadli.engine.state.piece.Piece

internal data class Player(
    val id: UByte,
    val name: String,
    val status: Status,
    val pieces: List<Piece>
)