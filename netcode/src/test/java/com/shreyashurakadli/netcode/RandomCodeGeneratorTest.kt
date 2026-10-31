package com.shreyashurakadli.netcode

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RandomCodeGeneratorTest {
    private val allowedChars = (('A'..'Z') + ('0'..'9')).toSet()

    @Test
    fun `generate returns a five character code`() {
        val code = RandomCodeGenerator.generate()

        assertEquals(CODE_LENGTH, code.length)
    }

    @Test
    fun `generate uses only uppercase letters and digits`() {
        repeat(DRAW_COUNT) {
            val code = RandomCodeGenerator.generate()

            assertTrue(
                code.all { it in allowedChars },
                "Code $code contains a character outside A-Z 0-9"
            )
        }
    }

    @Test
    fun `algorithm produces different code`() {
        val codes = List(DRAW_COUNT) { RandomCodeGenerator.generate() }.toSet()

        assertEquals(DRAW_COUNT, codes.size)
    }

    companion object {
        private const val CODE_LENGTH = 5
        private const val DRAW_COUNT = 100
    }
}
