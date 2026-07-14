package com.shreyashurakadli.engine.state.player

import com.shreyashurakadli.engine.state.piece.Piece

internal data class Player(
    val id: Int,
    val name: String,
    val status: PlayerStatus,
    val pieces: List<Piece>
)