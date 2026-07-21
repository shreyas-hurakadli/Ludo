package com.shreyashurakadli.engine

interface GameEngine {
    /**
     * Initializes the game.
     * Execute this before starting the game.
     * @param noOfPlayers Number of players
     * @param players List of players with their usernames
     */
    fun init(noOfPlayers: Int, players: List<String>)

    /**
     * Shows available options for the given dice value.
     * Returns a pair of player id and piece id(s).
     * @param diceValue Dice value
     */
    fun showAvailableOptions(diceValue: Int): Pair<Int, List<Int>>

    /**
     * Moves the piece.
     * @param piece Piece id
     * @param diceValue Dice value
     */
    fun movePiece(piece: Int, diceValue: Int)

    /**
     * Tells if the game is completed.
     */
    fun isGameCompleted(): Boolean
}