package com.shreyashurakadli.engine.rules.player

import com.shreyashurakadli.engine.rules.common.DiceValue
import com.shreyashurakadli.engine.rules.piece.PieceRules
import com.shreyashurakadli.engine.state.piece.Piece
import com.shreyashurakadli.engine.state.piece.PieceId
import com.shreyashurakadli.engine.state.player.Player
import com.shreyashurakadli.engine.state.player.PlayerId
import com.shreyashurakadli.engine.state.player.PlayerName
import com.shreyashurakadli.engine.state.player.PlayerStatus

internal class PlayerRulesEnforcer(
    private val pieceRulesEnforcer: PieceRules
) : PlayerRules {
    override fun updatePlayer(
        player: Player,
        diceValue: DiceValue,
        piece: Piece,
        partCount: Int
    ): Player {
        val newPlayerPieces = updatePlayerPieces(
            pieceId = piece.id,
            pieces = player.pieces,
            diceValue = diceValue,
            partCount = partCount
        )

        val newStatus = determineStatus(pieces = newPlayerPieces)

        return player.copy(
            pieces = newPlayerPieces,
            status = newStatus
        )
    }

    override fun playerHasWonStatus(player: Player): Boolean =
        player.status == PlayerStatus.Won

    override fun updatePlayerCapturedPiece(
        player: Player,
        otherPlayer: Player,
        piece: Piece,
        partCount: Int,
    ): Player {
        for (otherPiece in otherPlayer.pieces) {
            val isCaptured = pieceRulesEnforcer.isPieceCaptured(
                piece = piece,
                otherPiece = otherPiece,
                partCount = partCount,
                playerPart = player.id.toPart(),
                otherPlayerPart = otherPlayer.id.toPart()
            )

            if (isCaptured) {
                val newPieces = otherPlayer.pieces.map {
                    if (otherPiece.id == it.id) {
                        pieceRulesEnforcer.updateCapturedPiece(otherPiece)
                    } else {
                        it
                    }
                }

                return otherPlayer.copy(
                    pieces = newPieces,
                    status = determineStatus(pieces = newPieces)
                )
            }
        }

        return otherPlayer
    }

    override fun initializePlayer(players: List<PlayerName>): List<Player> =
        when {
            players.size == 2 -> createTwoPlayers(players)
            else -> createPlayersForGame(players)
        }

    override fun showAvailableOptions(
        player: Player,
        diceValue: DiceValue,
        quadrantCount: Int
    ): List<Piece> =
        pieceRulesEnforcer.canPiecesBeMoved(player.pieces, diceValue, quadrantCount)

    private fun createPlayersForGame(players: List<PlayerName>): List<Player> =
        players.mapIndexed { index, playerName ->
            Player(id = PlayerId(index), name = playerName)
        }

    /*
     * In a two-player game, players sit on opposite quadrants (0 and 2).
     * A player id is the quadrant the player owns, so the ids must not be sequential (0 and 1).
     * Sequential ids place the players on adjacent quadrants.
     * Capture checks would then match the wrong squares.
     */
    private fun createTwoPlayers(players: List<PlayerName>): List<Player> {
        val firstPlayer = Player(id = PlayerId(0), name = players.first())
        val secondPlayer = Player(id = PlayerId(2), name = players.last())
        return listOf(firstPlayer, secondPlayer)
    }

    private fun determineStatus(pieces: List<Piece>): PlayerStatus =
        if (pieceRulesEnforcer.hasFinishedAllPieces(pieces)) {
            PlayerStatus.Won
        } else {
            PlayerStatus.InProgress
        }

    private fun updatePlayerPieces(
        pieceId: PieceId,
        pieces: List<Piece>,
        diceValue: DiceValue,
        partCount: Int
    ): List<Piece> {
        val piece: Piece =
            pieces.find { it.id == pieceId } ?: throw IllegalStateException("Piece not found")

        val newPiece = pieceRulesEnforcer.updatePiece(
            piece = piece,
            diceValue = diceValue,
            partCount = partCount
        )

        val updatedPieces = pieces.map { piece ->
            if (piece.id == pieceId) {
                newPiece
            } else {
                piece
            }
        }

        return updatedPieces
    }
}
