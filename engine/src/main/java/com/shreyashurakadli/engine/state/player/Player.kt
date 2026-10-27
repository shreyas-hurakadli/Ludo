package com.shreyashurakadli.engine.state.player

import com.shreyashurakadli.engine.state.piece.Piece

@ConsistentCopyVisibility
data class Player internal constructor(
    val id: PlayerId,
    val name: PlayerName,
    val status: PlayerStatus = PlayerStatus.InProgress,
    val pieces: List<Piece> = Piece.initialPieces()
)