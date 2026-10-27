package com.shreyashurakadli.engine.rules.common

/**
 * Value shown on the dice.
 * @param value Range [1, 6]
 */
@JvmInline
value class DiceValue(val value: Int) {
    init {
        require(value in MIN_FACE..MAX_FACE) {
            "Dice value must be in range [$MIN_FACE, $MAX_FACE]. Received $value"
        }
    }

    companion object {
        private const val MIN_FACE = 1
        private const val MAX_FACE = 6
    }
}
