package com.shreyashurakadli.engine.rules.position.converter

import com.shreyashurakadli.engine.rules.position.common.DeterminePositionStatus
import com.shreyashurakadli.engine.rules.position.common.PositionIsBase
import com.shreyashurakadli.engine.rules.position.common.PositionIsNormal
import com.shreyashurakadli.engine.state.position.Position
import com.shreyashurakadli.engine.state.position.PositionStatus
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AbsoluteToRelativePositionConverterImplementationTest {
    val converter = AbsoluteToRelativePositionConverterImplementation(
        positionIsNormalPosition = PositionIsNormal(),
        determinePositionStatus = DeterminePositionStatus(),
        positionIsBase = PositionIsBase()
    )

    @Test
    fun convertPart0HomeToRelative() {
        val actualResult = converter.calculateRelativePosition(position = 57, partCount = 4)
        val expectedResult = Position(
            part = 0,
            tile = 18,
            status = PositionStatus.Safe
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0BaseToRelative() {
        val actualResult = converter.calculateRelativePosition(position = -1, partCount = 4)
        val expectedResult = Position(
            part = 0,
            tile = -1,
            status = PositionStatus.Safe
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0NormalToRelative() {
        val actualResult = converter.calculateRelativePosition(position = 8, partCount = 4)
        val expectedResult = Position(
            part = 0,
            tile = 8,
            status = PositionStatus.Safe
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart0FirstNormalToRelative() {
        val actualResult = converter.calculateRelativePosition(position = 0, partCount = 4)
        val expectedResult = Position(
            part = 0,
            tile = 0,
            status = PositionStatus.Unsafe
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart5NormalToRelative() {
        val actualResult = converter.calculateRelativePosition(position = 60, partCount = 6)
        val expectedResult = Position(
            part = 4,
            tile = 8,
            status = PositionStatus.Safe
        )

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun convertPart5HomeToRelative() {
        val actualResult = converter.calculateRelativePosition(position = 78, partCount = 6)
        val expectedResult = Position(
            part = 0,
            tile = 13,
            status = PositionStatus.Safe
        )

        assertEquals(expectedResult, actualResult)
    }
}