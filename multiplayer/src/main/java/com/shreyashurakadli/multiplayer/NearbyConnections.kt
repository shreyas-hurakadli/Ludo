package com.shreyashurakadli.multiplayer

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

internal class NearbyConnections(
    private val context: Context,
) : Transport() {
    companion object {
        private data class Message(
            val id: Long,
            val endpointId: String,
            val message: String
        )

        // Atomic updates are required because Nearby Connections callbacks are asynchronous
        private class SequenceGenerator {
            companion object {
                private val sequenceCounter = AtomicLong(0L)

                fun currentValue(): Long = sequenceCounter.get()

                fun generate(): Long = sequenceCounter.getAndIncrement()
            }
        }
    }

    private val connectionsClient = Nearby.getConnectionsClient(context)
    private val serviceId = context.packageName

    // Uses Mesh topology
    private val strategy = Strategy.P2P_CLUSTER

    /*
     * Queue is created to avoid multiple message being sent at the same time.
     * This is done to avoid resource contention.
     */
    private val messageQueue: MutableList<Message> = mutableListOf()

    private val isSendingPayload = AtomicBoolean(false)

    // Only discoverers use this callback
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            if (info.endpointName == clusterToken) {
                connect(peer = endpointId)
            }
        }

        override fun onEndpointLost(endpointId: String) {}
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        // Called when a connection is initiated. Both sides must accept or reject the connection.
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        // Called when the connection response is received, indicating success or failure.
        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when {
                result.status.isSuccess -> handleSuccessfulConnection(endpointId)
            }
        }

        private fun handleSuccessfulConnection(endpointId: String) {
            _connectedEndpoints.update { current ->
                val endpoint = discoveredEndpoints.find { it.id == endpointId }
                val endpointIsNotNull = (endpoint != null)

                when {
                    endpointIsNotNull -> current + (endpoint to Connection())
                    else -> current
                }
            }
        }

        // Called when the connection is lost.
        override fun onDisconnected(endpointId: String) {
            _connectedEndpoints.update { current ->
                val endpoint = current.keys.find { it.id == endpointId }
                val endpointDoesNotExist = endpoint == null

                when {
                    endpointDoesNotExist -> current
                    else -> current - endpoint
                }
            }
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receivedPayload = convertPayloadToString(payload)

            _connectedEndpoints.update { current ->
                val endpoint = current.keys.find { it.id == endpointId } ?: return
                val endpointConnection = current[endpoint] ?: return
                current + (endpoint to endpointConnection.copy(receivedPayload = receivedPayload))
            }
        }


        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            when (update.status) {
                PayloadTransferUpdate.Status.IN_PROGRESS -> {}
                else -> handlePayloadTransferUpdate()
            }
        }
    }

    private fun handlePayloadTransferUpdate() {
        isSendingPayload.set(false)
        val message = getFromMessageQueue() ?: return
        sendPayload(message.endpointId, message.message)
    }

    private fun addToMessageQueue(endpointId: String, message: String) {
        synchronized(messageQueue) {
            val isRedundant = messageQueue
                .any { it.endpointId == endpointId && it.message == message }

            if (isRedundant) {
                return
            }

            val message = Message(
                id = SequenceGenerator.generate(),
                endpointId = endpointId,
                message = message
            )

            messageQueue.add(message)
        }
    }

    private fun getFromMessageQueue(): Message? {
        synchronized(messageQueue) {
            while (messageQueue.isNotEmpty()) {
                val message = messageQueue.first()
                val isConnected = _connectedEndpoints.value.keys.any { it.id == message.endpointId }

                if (isConnected) {
                    return messageQueue.removeAt(0)
                } else {
                    messageQueue.removeAt(0)
                }
            }
            return null
        }
    }

    private val advertisingOptions = AdvertisingOptions
        .Builder()
        .setStrategy(strategy)
        .build()

    private fun startAdvertising(name: String) {
        /*
         * Mutual exclusion is implemented for advertising and discovery.
         * This is done to mitigate Radio Resource Contention (Medium conflict).
         */
        stopDiscovery()

        connectionsClient.startAdvertising(
            name,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        )
            .addOnFailureListener { }
    }

    private fun stopAdvertising() {
        connectionsClient.stopAdvertising()
    }

    private val discoveryOptions = DiscoveryOptions
        .Builder()
        .setStrategy(strategy)
        .build()

    private fun startDiscovery() {
        /*
         * Mutual exclusion is implemented for advertising and discovery.
         * This is done to mitigate Radio Resource Contention (Medium conflict).
         */
        stopAdvertising()

        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            discoveryOptions
        )
            .addOnFailureListener { }
    }

    private fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }

    override fun checkPrerequisites() {
        // Check Permissions
        val requiredPermissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw SecurityException("Permission $permission not granted")
            }
        }

        // Check Bluetooth
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            throw IllegalStateException("Bluetooth is required but disabled or not available")
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            throw IllegalStateException("Bluetooth Low Energy (BLE) is not supported on this device")
        }

        // Check Wi-Fi
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            throw IllegalStateException("Wi-Fi is required but disabled")
        }

        // Check Location (Required for Nearby Connections on many devices)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE)
                as LocationManager
        if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
            throw IllegalStateException("Location is required but disabled")
        }
    }

    override fun connect(peer: String) {
        connectionsClient.requestConnection(
            localName ?: throw IllegalArgumentException("Details have not been registered"),
            peer,
            connectionLifecycleCallback
        )
    }

    override fun disconnect(peer: String) {
        connectionsClient.disconnectFromEndpoint(peer)
    }

    override fun sendPayload(peer: String, message: String) {
        val payload = convertStringToPayload(message)

        val payloadIsNotBeingSent = isSendingPayload.compareAndSet(false, true)
        if (!payloadIsNotBeingSent) {
            addToMessageQueue(peer, message)
            return
        }

        connectionsClient.sendPayload(
            peer,
            payload
        )
            .addOnFailureListener { handlePayloadTransferUpdate() }
            .addOnCanceledListener { handlePayloadTransferUpdate() }
    }

    private fun convertPayloadToString(payload: Payload): String =
        payload.asBytes()?.decodeToString() ?: ""

    private fun convertStringToPayload(message: String): Payload =
        Payload.fromBytes(message.toByteArray())

    override fun close() {
        stopAdvertising()
        stopDiscovery()
        connectionsClient.stopAllEndpoints()
    }
}
