package com.shreyashurakadli.multiplayer

internal sealed class Endpoint(
    open val id: String,
    open val name: String
)

internal data class NormalEndpoint(
    override val id: String,
    override val name: String
) : Endpoint(id, name)

internal data class NearbyConnectionsEndpoint(
    override val id: String,
    override val name: String,
    val authenticationToken: String
) : Endpoint(id, name)