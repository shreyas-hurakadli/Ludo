package com.shreyashurakadli.multiplayer.transport

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy

internal class NearbyConnections(
    context: Context,
    name: String,
    token: String
) : Transport(name, token) {

    private val appContext = context.applicationContext
    private val connectionsClient: ConnectionsClient = Nearby.getConnectionsClient(appContext)
    private val serviceId: String = appContext.packageName
    private val strategy: Strategy = Strategy.P2P_CLUSTER

    private val endpointDisplayNames = mutableMapOf<String, String>()
    private val endpointAdvertisedNames = mutableMapOf<String, String>()

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            payload.asBytes()?.decodeToString() ?: return
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) = Unit
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            rememberEndpointName(endpointId, info.endpointName)
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when {
                result.status.isSuccess -> addConnectedPeer(endpointId)
                else -> forgetEndpoint(endpointId)
            }
        }

        override fun onDisconnected(endpointId: String) {
            removePeerById(endpointId)
            forgetEndpoint(endpointId)
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            endpointAdvertisedNames[endpointId] = info.endpointName
            if (info.endpointName == token) {
                connect(endpointId)
            }
        }

        override fun onEndpointLost(endpointId: String) {
            forgetEndpoint(endpointId)
        }
    }

    override fun checkPrerequisites() = Unit

    override fun start(role: Role, peerCount: UInt?) {
        require(token.isNotBlank()) { "Room code must not be blank" }
        when (role) {
            Role.Host -> {
                requireNotNull(peerCount) { "Host must provide expected peer count" }
                setExpectedPeerCount(peerCount)
                stopDiscovery()
                connectionsClient.startAdvertising(
                    token,
                    serviceId,
                    connectionLifecycleCallback,
                    AdvertisingOptions.Builder()
                        .setStrategy(strategy)
                        .build()
                )
            }
            Role.Guest -> {
                require(peerCount == null) { "Guest must not set peer count" }
                setExpectedPeerCount(null)
                stopAdvertising()
                connectionsClient.startDiscovery(
                    serviceId,
                    endpointDiscoveryCallback,
                    DiscoveryOptions.Builder()
                        .setStrategy(strategy)
                        .build()
                )
            }
        }
    }

    override fun connect(peerId: String) {
        rememberEndpointName(
            peerId,
            endpointAdvertisedNames[peerId] ?: name
        )
        connectionsClient.requestConnection(name, peerId, connectionLifecycleCallback)
    }

    override fun disconnect(peerId: String) {
        connectionsClient.disconnectFromEndpoint(peerId)
    }

    override fun sendPayload(peerId: String, message: String) = Unit

    override fun close() {
        stopAdvertising()
        stopDiscovery()
        connectionsClient.stopAllEndpoints()
        endpointDisplayNames.clear()
        endpointAdvertisedNames.clear()
        clearPeers()
        setExpectedPeerCount(null)
    }

    private fun addConnectedPeer(endpointId: String) {
        val displayName = endpointDisplayNames[endpointId] ?: endpointId
        val advertisedName = endpointAdvertisedNames[endpointId]
        upsertPeer(
            Peer(
                id = endpointId,
                displayName = displayName,
                advertisedName = advertisedName
            )
        )
    }

    private fun rememberEndpointName(endpointId: String, endpointName: String) {
        endpointDisplayNames[endpointId] = endpointName
    }

    private fun forgetEndpoint(endpointId: String) {
        endpointDisplayNames.remove(endpointId)
        endpointAdvertisedNames.remove(endpointId)
    }

    private fun stopAdvertising() {
        connectionsClient.stopAdvertising()
    }

    private fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }
}
