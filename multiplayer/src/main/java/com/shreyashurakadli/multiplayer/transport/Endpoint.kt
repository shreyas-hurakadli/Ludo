package com.shreyashurakadli.multiplayer.transport

sealed class Endpoint(
    open val id: String,
    open val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Endpoint) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

data class NormalEndpoint(
    override val id: String,
    override val name: String
) : Endpoint(id, name)

data class NearbyConnectionsEndpoint(
    override val id: String,
    override val name: String,
    val authenticationToken: String
) : Endpoint(id, name)