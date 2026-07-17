package com.shreyashurakadli.engine.rules.piece.common

import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceStatus

internal class PieceHasFinishedStatus {
    operator fun invoke(piece: Piece): Boolean =
        piece.status == PieceStatus.Finished
}