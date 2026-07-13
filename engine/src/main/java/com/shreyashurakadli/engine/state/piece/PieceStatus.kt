package com.shreyashurakadli.engine.state.piece

internal interface PieceStatus {
    object InBase : PieceStatus
    object OnBoard : PieceStatus
    object Finished : PieceStatus
}