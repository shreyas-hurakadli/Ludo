package com.shreyashurakadli.multiplayer

sealed interface Protocol {
    data object NearbyConnections : Protocol
}