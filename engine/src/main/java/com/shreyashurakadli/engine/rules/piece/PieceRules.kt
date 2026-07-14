package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.state.piece.Piece

internal interface PieceRules {
    fun updatePiece(piece: Piece, diceValue: Int, playerCount: Int): Piece
    fun hasFinishedAllPieces(pieces: List<Piece>): Boolean
}