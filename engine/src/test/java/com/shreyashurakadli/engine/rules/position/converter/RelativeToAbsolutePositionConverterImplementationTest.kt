package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RelativeToAbsolutePositionConverterImplementationTest {
    private val converter = RelativeToAbsolutePositionConverterImplementation(
        positionIsNormalPosition = PositionIsNormal(),
        positionIsBase = PositionIsBase(),
    )

    @Test
    fun convertPart0HomeToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = 18,
                status = PositionStatus.Safe
            ),
            partCount = 4,
            playerPart = 0
        )
        val expectedResult = 57

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0BaseToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = -1,
                status = PositionStatus.Safe
            ),
            partCount = 4,
            playerPart = 0
        )
        val expectedResult = -1

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0NormalToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = 8,
                status = PositionStatus.Safe
            ),
            partCount = 4,
            playerPart = 0
        )
        val expectedResult = 8

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0FirstNormalToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = 0,
                status = PositionStatus.Unsafe
            ),
            partCount = 4,
            playerPart = 0
        )
        val expectedResult = 0

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart4NormalToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 4,
                tile = 8,
                status = PositionStatus.Safe
            ),
            partCount = 6,
            playerPart = 0
        )
        val expectedResult = 60

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0HomeWithDifferentPartCountToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = 13,
                status = PositionStatus.Safe
            ),
            partCount = 6,
            playerPart = 0
        )
        val expectedResult = 78

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0NormalForPlayer1ToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = 8,
                status = PositionStatus.Safe
            ),
            partCount = 4,
            playerPart = 1
        )
        val expectedResult = 21

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0BaseForPlayer1ToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = -1,
                status = PositionStatus.Safe
            ),
            partCount = 4,
            playerPart = 1
        )
        val expectedResult = -2

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0BaseForPlayer3ToAbsolute() {
        val actualResult = converter.calculateAbsolutePosition(
            position = Position(
                part = 0,
                tile = -1,
                status = PositionStatus.Safe
            ),
            partCount = 4,
            playerPart = 3
        )
        val expectedResult = -4

        assertEquals(expectedResult, actualResult)
    }
}
