package com.shreyashurakadli.netcode

/**
 * Role this device plays in the room.
 * Owned by :netcode so callers never name :multiplayer types.
 */
sealed interface Role {
    data object Host : Role
    data object Guest : Role
}
