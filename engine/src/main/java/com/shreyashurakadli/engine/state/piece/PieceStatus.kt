package com.shreyashurakadli.engine.state.piece

interface PieceStatus {
    object InBase : PieceStatus
    object OnBoard : PieceStatus
    object Finished : PieceStatus
}