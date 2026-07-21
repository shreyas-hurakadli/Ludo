package com.shreyashurakadli.engine

interface GameEngine {
    /**
     * Initializes the game.
     * Execute this before starting the game.
     * @param noOfPlayers
     * @param players List of players with their usernames
     */
    fun init(noOfPlayers: Int, players: List<String>)

    /**
     * Shows available options for the given dice value.
     * Returns a pair of player id and piece id(s).
     * @param diceValue
     */
    fun showAvailableOptions(diceValue: Int): Pair<Int, List<Int>>

    /**
     * Moves the piece.
     * @param pieceId
     * @param diceValue
     */
    fun movePiece(pieceId: Int, diceValue: Int)

    /**
     * Tells if the game is completed.
     */
    fun isGameCompleted(): Boolean
}