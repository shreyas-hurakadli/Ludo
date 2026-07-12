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

    @Test
    fun updatePosition_enteringHomePosition() {
        val state = Position(
            part = 0,
            tile = 3,
            status = Status.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 4,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 13,
            status = Status.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_notEnteringHomePosition() {
        val state = Position(
            part = 0,
            tile = 3,
            status = Status.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 3,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 6,
            status = Status.Unsafe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_EnteringFinishPositionFromLastNonHomePosition() {
        val state = Position(
            part = 0,
            tile = 6,
            status = Status.Unsafe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 6,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 18,
            status = Status.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_EnteringFinishPositionFromHomePosition() {
        val state = Position(
            part = 0,
            tile = 13,
            status = Status.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 5,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 18,
            status = Status.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_EnteringAnotherPart() {
        val state = Position(
            part = 0,
            tile = 12,
            status = Status.Unsafe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 1,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 1,
            tile = 0,
            status = Status.Unsafe
        )
        assertEquals(expectedPosition, actualPosition)
    }
}