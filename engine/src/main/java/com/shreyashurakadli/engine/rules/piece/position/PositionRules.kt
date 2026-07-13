package com.shreyashurakadli.engine.rules.piece.position

import com.shreyashurakadli.engine.state.piece.position.Position

internal interface PositionRules {
    fun updatePosition(state: Position, diceValue: Int, playerCount: Int): Position

    fun isAtBasePosition(tile: Int): Boolean
    fun isAtFinalPosition(tile: Int): Boolean
}