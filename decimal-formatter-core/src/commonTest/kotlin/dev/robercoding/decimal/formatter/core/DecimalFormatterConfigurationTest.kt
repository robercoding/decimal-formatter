package dev.robercoding.decimal.formatter.core

import dev.robercoding.decimal.formatter.model.DecimalSeparator
import dev.robercoding.decimal.formatter.model.ThousandSeparator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DecimalFormatterConfigurationTest {

    // ===== Valid Configuration Tests =====

    @Test
    fun `valid configuration does not throw exception`() {
        // Should not throw any exceptions
        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = 2,
            maxDigits = 10
        )

        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = 0,
            maxDigits = 5
        )

        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.NONE,
            decimalPlaces = 3,
            maxDigits = 15
        )
    }

    // ===== maxDigits Validation Tests =====

    @Test
    fun `maxDigits must be greater than 0`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.COMMA,
                decimalPlaces = 2,
                maxDigits = 0
            )
        }
        assertEquals("Max digits must be higher than 0", exception.message)
    }

    @Test
    fun `negative maxDigits throws exception`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.COMMA,
                decimalPlaces = 2,
                maxDigits = -1
            )
        }
        assertEquals("Max digits must be higher than 0", exception.message)
    }

    // ===== decimalPlaces Validation Tests =====

    @Test
    fun `negative decimalPlaces throws exception`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.COMMA,
                decimalPlaces = -1,
                maxDigits = 10
            )
        }
        assertEquals("Decimal places must be non-negative", exception.message)
    }

    @Test
    fun `zero decimalPlaces is valid`() {
        // Should not throw - zero decimal places is valid
        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = 0,
            maxDigits = 10
        )
    }

    // ===== decimalPlaces vs maxDigits Validation Tests =====

    @Test
    fun `decimalPlaces equal to maxDigits throws exception`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.COMMA,
                decimalPlaces = 10,
                maxDigits = 10
            )
        }
        assertEquals("Decimal places must be less than max digits", exception.message)
    }

    @Test
    fun `decimalPlaces greater than maxDigits throws exception`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.COMMA,
                decimalPlaces = 15,
                maxDigits = 10
            )
        }
        assertEquals("Decimal places must be less than max digits", exception.message)
    }

    @Test
    fun `decimalPlaces one less than maxDigits is valid`() {
        // Should not throw - decimal places can be maxDigits - 1
        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = 9,
            maxDigits = 10
        )
    }

    // ===== Separator Conflict Validation Tests =====

    @Test
    fun `same thousand and decimal separator throws exception - dot case`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.DOT,
                decimalPlaces = 2,
                maxDigits = 10
            )
        }
        assertEquals("Thousand separator and decimal separator must be different", exception.message)
    }

    @Test
    fun `same thousand and decimal separator throws exception - comma case`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration(
                decimalSeparator = DecimalSeparator.COMMA,
                thousandSeparator = ThousandSeparator.COMMA,
                decimalPlaces = 2,
                maxDigits = 10
            )
        }
        assertEquals("Thousand separator and decimal separator must be different", exception.message)
    }

    @Test
    fun `thousand separator NONE with any decimal separator is valid`() {
        // Should not throw - NONE thousand separator is always valid
        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.NONE,
            decimalPlaces = 2,
            maxDigits = 10
        )

        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.NONE,
            decimalPlaces = 2,
            maxDigits = 10
        )
    }

    @Test
    fun `different separators are valid`() {
        // Should not throw - different separators are valid
        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = 2,
            maxDigits = 10
        )

        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = 2,
            maxDigits = 10
        )

        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.APOSTROPHE,
            decimalPlaces = 2,
            maxDigits = 10
        )
    }

    // ===== Companion Object Factory Methods Tests =====

    @Test
    fun `european factory method creates valid configuration`() {
        val config = DecimalFormatterConfiguration.european()

        assertEquals(DecimalSeparator.COMMA, config.decimalSeparator)
        assertEquals(ThousandSeparator.DOT, config.thousandSeparator)
        assertEquals(2, config.decimalPlaces)
        assertEquals(10, config.maxDigits) // Default maxDigits
    }

    @Test
    fun `european factory method with custom decimal places`() {
        val config = DecimalFormatterConfiguration.european(decimalPlaces = 3)

        assertEquals(DecimalSeparator.COMMA, config.decimalSeparator)
        assertEquals(ThousandSeparator.DOT, config.thousandSeparator)
        assertEquals(3, config.decimalPlaces)
    }

    @Test
    fun `us factory method creates valid configuration`() {
        val config = DecimalFormatterConfiguration.us()

        assertEquals(DecimalSeparator.DOT, config.decimalSeparator)
        assertEquals(ThousandSeparator.COMMA, config.thousandSeparator)
        assertEquals(2, config.decimalPlaces)
    }

    @Test
    fun `us factory method with custom decimal places`() {
        val config = DecimalFormatterConfiguration.us(decimalPlaces = 0)

        assertEquals(DecimalSeparator.DOT, config.decimalSeparator)
        assertEquals(ThousandSeparator.COMMA, config.thousandSeparator)
        assertEquals(0, config.decimalPlaces)
    }

    @Test
    fun `swiss factory method creates valid configuration`() {
        val config = DecimalFormatterConfiguration.swiss()

        assertEquals(DecimalSeparator.DOT, config.decimalSeparator)
        assertEquals(ThousandSeparator.APOSTROPHE, config.thousandSeparator)
        assertEquals(2, config.decimalPlaces)
    }

    @Test
    fun `plain factory method creates valid configuration`() {
        val config = DecimalFormatterConfiguration.plain()

        assertEquals(DecimalSeparator.DOT, config.decimalSeparator)
        assertEquals(ThousandSeparator.NONE, config.thousandSeparator)
        assertEquals(2, config.decimalPlaces)
    }

    @Test
    fun `default configuration is valid`() {
        val config = DecimalFormatterConfiguration.DefaultConfiguration

        assertEquals(DecimalSeparator.COMMA, config.decimalSeparator)
        assertEquals(ThousandSeparator.DOT, config.thousandSeparator)
        assertEquals(2, config.decimalPlaces)
        assertEquals(10, config.maxDigits)
    }

    // ===== Factory Methods Validation Edge Cases =====

    @Test
    fun `factory method with invalid decimal places throws exception`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration.us(decimalPlaces = -1)
        }
        assertEquals("Decimal places must be non-negative", exception.message)
    }

    @Test
    fun `factory method with decimal places too large throws exception`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            DecimalFormatterConfiguration.european(decimalPlaces = 10) // Default maxDigits is 10
        }
        assertEquals("Decimal places must be less than max digits", exception.message)
    }
}