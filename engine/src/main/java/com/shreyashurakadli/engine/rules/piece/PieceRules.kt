package com.shreyashurakadli.engine.rules.piece

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.position.Part

internal interface PieceRules {
    fun updatePiece(piece: Piece, diceValue: DiceValue, partCount: Int): Piece
    fun hasFinishedAllPieces(pieces: List<Piece>): Boolean
    fun isPieceCaptured(piece: Piece, otherPiece: Piece, partCount: Int, playerPart: Part, otherPlayerPart: Part): Boolean
    fun updateCapturedPiece(piece: Piece): Piece
    fun canPiecesBeMoved(pieces: List<Piece>, diceValue: DiceValue, quadrantCount: Int): List<Piece>
}
