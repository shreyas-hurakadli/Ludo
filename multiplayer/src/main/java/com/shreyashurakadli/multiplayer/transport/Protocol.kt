package com.shreyashurakadli.multiplayer.transport

sealed interface Protocol {
    data object NearbyConnections : Protocol
}