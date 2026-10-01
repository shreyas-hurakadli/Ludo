package com.shreyashurakadli.multiplayer

sealed interface PrerequisitesResult {
    data object Ready : PrerequisitesResult

    data class MissingPermissions(val permissions: List<String>) : PrerequisitesResult

    data object BluetoothUnavailable : PrerequisitesResult

    data object BluetoothDisabled : PrerequisitesResult

    data object WifiDisabled : PrerequisitesResult

    data object LocationDisabled : PrerequisitesResult

    data object BleNotSupported : PrerequisitesResult
}
