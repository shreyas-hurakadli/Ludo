package com.shreyashurakadli.netcode

import com.shreyashurakadli.netcode.RandomCodeGenerator.random
import java.security.SecureRandom

object RandomCodeGenerator {
    private const val CODE_LENGTH = 5
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    private val random = SecureRandom()

    /**
     * Generates a random [CODE_LENGTH] character uppercase alphanumeric room code.
     */
    fun generate(): String = buildString(CODE_LENGTH) {
        repeat(CODE_LENGTH) {
            append(ALPHABET[random.nextInt(ALPHABET.length)])
        }
    }
}
