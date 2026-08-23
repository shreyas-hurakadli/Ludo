package com.shreyashurakadli.multiplayer.transport

sealed class Endpoint(
    open val id: String,
    open val name: String
)

@ConsistentCopyVisibility
data class NormalEndpoint protected constructor(
    override val id: String,
    override val name: String
) : Endpoint(id, name)

@ConsistentCopyVisibility
data class NearbyConnectionsEndpoint protected constructor(
    override val id: String,
    override val name: String,
    val authenticationToken: String
) : Endpoint(id, name)