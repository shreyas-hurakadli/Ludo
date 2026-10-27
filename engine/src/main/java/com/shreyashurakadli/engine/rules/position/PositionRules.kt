package com.shreyashurakadli.engine.rules.position

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.state.position.Position

internal interface PositionRules {
    fun updatePosition(state: Position, diceValue: DiceValue, quadrantCount: Int): Position
    fun canPositionBeValid(currentState: Position, diceValue: DiceValue, quadrantCount: Int): Boolean
    fun isAtBasePosition(tile: Int): Boolean
    fun isAtFinalPosition(tile: Int): Boolean
    fun provideBasePosition(): Position
}
