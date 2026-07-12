package com.shreyashurakadli.engine.rules.piece.position

import com.shreyashurakadli.engine.state.piece.position.Position
import com.shreyashurakadli.engine.state.piece.position.Status
import kotlin.test.Test
import kotlin.test.assertEquals

class PositionRulesEnforcerTest {
    private val positionRulesEnforcer = PositionRulesEnforcer()

    @Test
    fun updatePosition_enteringBoard() {
        val state = Position(
            part = 0,
            tile = -1,
            status = Status.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 6,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 8,
            status = Status.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }
}