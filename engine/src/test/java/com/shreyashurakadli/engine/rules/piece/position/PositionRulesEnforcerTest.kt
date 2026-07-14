package com.shreyashurakadli.engine.rules.piece.position

import com.shreyashurakadli.engine.state.piece.position.Position
import com.shreyashurakadli.engine.state.piece.position.PositionStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PositionRulesEnforcerTest {
    private val positionRulesEnforcer = PositionRulesEnforcer()

    @Test
    fun updatePosition_enteringBoard() {
        val state = Position(
            part = 0,
            tile = -1,
            status = PositionStatus.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 6,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 8,
            status = PositionStatus.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_enteringHomePosition() {
        val state = Position(
            part = 0,
            tile = 3,
            status = PositionStatus.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 4,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 13,
            status = PositionStatus.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_notEnteringHomePosition() {
        val state = Position(
            part = 0,
            tile = 3,
            status = PositionStatus.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 3,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 6,
            status = PositionStatus.Unsafe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_EnteringFinishPositionFromLastNonHomePosition() {
        val state = Position(
            part = 0,
            tile = 6,
            status = PositionStatus.Unsafe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 6,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 18,
            status = PositionStatus.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_EnteringFinishPositionFromHomePosition() {
        val state = Position(
            part = 0,
            tile = 13,
            status = PositionStatus.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 5,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = 18,
            status = PositionStatus.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_EnteringAnotherPart() {
        val state = Position(
            part = 0,
            tile = 12,
            status = PositionStatus.Unsafe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 1,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 1,
            tile = 0,
            status = PositionStatus.Unsafe
        )
        assertEquals(expectedPosition, actualPosition)
    }

    @Test
    fun updatePosition_shouldNotEnterBoard() {
        val state = Position(
            part = 0,
            tile = -1,
            status = PositionStatus.Safe
        )
        val actualPosition = positionRulesEnforcer.updatePosition(
            state = state,
            diceValue = 1,
            playerCount = 4
        )
        val expectedPosition = Position(
            part = 0,
            tile = -1,
            status = PositionStatus.Safe
        )
        assertEquals(expectedPosition, actualPosition)
    }
}