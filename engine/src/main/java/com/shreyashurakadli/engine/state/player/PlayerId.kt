package com.shreyashurakadli.engine.state.player

import com.shreyashurakadli.engine.state.position.Part

/**
 * Identifier of a player. Equal to the quadrant the player owns.
 * @param value Must not be negative
 */
@JvmInline
value class PlayerId(val value: Int) {
    init {
        require(value >= MIN_ID) {
            "Player id must not be negative. Received $value"
        }
    }

    /**
     * Converts this player id to the board quadrant owned by the player.
     */
    fun toPart(): Part = Part(value)

    companion object {
        private const val MIN_ID = 0
    }
}
