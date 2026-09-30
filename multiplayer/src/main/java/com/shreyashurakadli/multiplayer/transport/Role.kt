package com.shreyashurakadli.multiplayer.transport

sealed interface Role {
    data object Host : Role
    data object Guest : Role
}