package com.shreyashurakadli.engine.constants

internal class PositionConstants {
    companion object {
        const val BASE_POSITION = -1
        const val START_POSITION = 8
        const val FINAL_POSITION = 18
        const val SPECIAL_SAFE_POSITION = 3
        const val HOME_POSITION_0 = 13
        const val HOME_POSITION_1 = 14
        const val HOME_POSITION_2 = 15
        const val HOME_POSITION_3 = 16
        const val HOME_POSITION_4 = 17
        const val LAST_NON_HOME_POSITION = 6
        val SAFE_POSITIONS =
            arrayOf(BASE_POSITION, START_POSITION, FINAL_POSITION, SPECIAL_SAFE_POSITION)
        val HOME_POSITIONS =
            arrayOf(
                HOME_POSITION_0,
                HOME_POSITION_1,
                HOME_POSITION_2,
                HOME_POSITION_3,
                HOME_POSITION_4,
            )
        const val TOTAL_POSITIONS_IN_PART = 13

        const val START_PART = 0
    }
}