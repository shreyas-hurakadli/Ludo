package com.shreyashurakadli.engine.state.player

import com.shreyashurakadli.engine.state.piece.Piece

@ConsistentCopyVisibility
data class Player internal constructor(
    val id: Int,
    val name: String,
    val status: PlayerStatus = PlayerStatus.InProgress,
    val pieces: List<Piece> = Piece.initialPieces()
)