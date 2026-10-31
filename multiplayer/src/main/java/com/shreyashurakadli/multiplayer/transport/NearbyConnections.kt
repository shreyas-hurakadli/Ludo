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
import com.shreyashurakadli.multiplayer.PrerequisitesResult
import com.shreyashurakadli.multiplayer.ReceivedMessage

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
            val content = payload.asBytes()?.decodeToString() ?: return
            publishIncomingMessage(
                ReceivedMessage(
                    senderId = endpointId,
                    content = content
                )
            )
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
            removePeer(endpointId)
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

    private fun startAdvertising() {
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

    private fun startDiscovery() {
        stopAdvertising()
        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            DiscoveryOptions.Builder()
                .setStrategy(strategy)
                .build()
        )
    }

    override fun checkPrerequisites(): PrerequisitesResult =
        NearbyPrerequisites.check(appContext)

    override fun start(role: Role, peerCount: UInt) {
        require(token.isNotBlank()) { "Room code must not be blank" }
        setExpectedPeerCount(peerCount)
        when (role) {
            Role.Host -> startAdvertising()
            Role.Guest -> startDiscovery()
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
        cancelIncomingMessage()
        endpointDisplayNames.clear()
        endpointAdvertisedNames.clear()
        clearPeers()
        setExpectedPeerCount(0U)
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
