package com.shreyashurakadli.engine.state.position

/**
 * Index of a quadrant on the board.
 * @param value Must not be negative
 */
@JvmInline
value class Part(val value: Int) {
    init {
        require(value >= MIN_PART) {
            "Part must not be negative. Received $value"
        }
    }

    companion object {
        private const val MIN_PART = 0
    }
}
