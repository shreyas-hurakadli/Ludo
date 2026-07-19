package com.shreyashurakadli.engine.rules.position.common

import com.shreyashurakadli.engine.constants.PositionConstants
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus

internal class GetBasePositionPiece {
    operator fun invoke(piece: Piece): Piece =
        piece.copy(
            position = Position(
                part = PositionConstants.START_PART,
                tile = PositionConstants.BASE_POSITION,
                status = PositionStatus.Safe
            )
        )
}