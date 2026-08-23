package com.shreyashurakadli.multiplayer

/**
 * Represents the role of the node in the group
 */
sealed interface Role {
    data object Owner : Role
    data object Guest : Role
}