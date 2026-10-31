package com.shreyashurakadli.multiplayer

/**
 * A message received from a connected peer.
 *
 * @param senderId endpoint id of the peer that sent the message.
 * @param content decoded payload text.
 */
data class ReceivedMessage(
    val senderId: String,
    val content: String
)
