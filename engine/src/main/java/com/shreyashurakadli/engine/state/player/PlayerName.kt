package com.shreyashurakadli.engine.state.player

/**
 * Name of a player as shown in the game.
 * @param value Must not be blank
 */
@JvmInline
value class PlayerName(val value: String) {
    init {
        require(value.isNotBlank()) {
            "Player name must not be blank"
        }
    }
}
