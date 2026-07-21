package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.state.piece.Piece

internal interface PieceRules {
    fun updatePiece(piece: Piece, diceValue: Int, partCount: Int): Piece
    fun hasFinishedAllPieces(pieces: List<Piece>): Boolean
    fun isPieceCaptured(piece: Piece, otherPiece: Piece, partCount: Int, playerPart: Int, otherPlayerPart: Int): Boolean
    fun updateCapturedPiece(piece: Piece): Piece
    fun canPiecesBeMoved(pieces: List<Piece>, diceValue: Int, quadrantCount: Int): List<Piece>
}