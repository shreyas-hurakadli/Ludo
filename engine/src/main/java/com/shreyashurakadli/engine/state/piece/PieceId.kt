package com.shreyashurakadli.engine.state.piece

/**
 * Identifier of a piece within a player's pieces.
 * @param value Range [0, 3]
 */
@JvmInline
value class PieceId(val value: Int) {
    init {
        require(value in MIN_ID..MAX_ID) {
            "Piece id must be in range [$MIN_ID, $MAX_ID]. Received $value"
        }
    }

    companion object {
        private const val MIN_ID = 0
        private const val MAX_ID = 3
    }
}
