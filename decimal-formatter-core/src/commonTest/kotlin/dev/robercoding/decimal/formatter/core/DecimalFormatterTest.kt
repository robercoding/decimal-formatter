package dev.robercoding.decimal.formatter.core

import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import kotlin.test.*
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator
import dev.robercoding.decimal.formatter.core.model.DecimalSeparator

class DecimalFormatterTest {

    // Test configurations for different scenarios
    private val usConfiguration = DecimalFormatterConfiguration.us()

    private val europeanConfiguration = DecimalFormatterConfiguration.european()

    private val swissConfiguration = DecimalFormatterConfiguration.swiss()

    private val plainConfiguration = DecimalFormatterConfiguration.plain()

    private val wholeNumberConfiguration = DecimalFormatterConfiguration(
        decimalPlaces = 0,
        thousandSeparator = ThousandSeparator.COMMA,
        decimalSeparator = DecimalSeparator.DOT,
        maxDigits = 12
    )

    // ===== Basic Formatting Tests =====

    @Test
    fun `format basic number with US configuration`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("1,234.56", formatter.format("123456"))
        assertEquals("12.34", formatter.format("1234"))
        assertEquals("0.12", formatter.format("12"))
        assertEquals("0.01", formatter.format("1"))
    }

    @Test
    fun `format basic number with European configuration`() {
        val formatter = DecimalFormatter(europeanConfiguration)

        assertEquals("1.234,56", formatter.format("123456"))
        assertEquals("12,34", formatter.format("1234"))
        assertEquals("0,12", formatter.format("12"))
        assertEquals("0,01", formatter.format("1"))
    }

    @Test
    fun `format basic number with Swiss configuration`() {
        val formatter = DecimalFormatter(swissConfiguration)

        assertEquals("1'234.56", formatter.format("123456"))
        assertEquals("12.34", formatter.format("1234"))
        assertEquals("0.12", formatter.format("12"))
        assertEquals("0.01", formatter.format("1"))
    }

    // ===== Edge Cases =====

    @Test
    fun `format empty string returns zero`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("0.00", formatter.format(""))
    }

    @Test
    fun `format zero returns formatted zero`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("0.00", formatter.format("0"))
        assertEquals("0.00", formatter.format("00"))
        assertEquals("0.00", formatter.format("000"))
    }

    @Test
    fun `format single digit pads with leading zero`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("0.01", formatter.format("1"))
        assertEquals("0.05", formatter.format("5"))
        assertEquals("0.09", formatter.format("9"))
    }

    @Test
    fun `format removes leading zeros correctly`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("1,234.56", formatter.format("00123456"))
        assertEquals("12.34", formatter.format("001234"))
        assertEquals("0.12", formatter.format("0012"))
    }

    // ===== Large Numbers =====

    @Test
    fun `format large numbers with multiple thousand separators`() {
        val formatter = DecimalFormatter(usConfiguration.copy(maxDigits = 12))

        assertEquals("1,234,567.89", formatter.format("123456789"))
        assertEquals("12,345,678.90", formatter.format("1234567890"))
        assertEquals("123,456,789.01", formatter.format("12345678901"))
    }

    @Test
    fun `format very large numbers`() {
        val expectedValue = "1,234,567,890.12"
        val formatter = DecimalFormatter(usConfiguration.copy(maxDigits = expectedValue.length))

        assertEquals(expectedValue, formatter.format("123456789012"))
    }

    // ===== No Thousand Separator =====

    @Test
    fun `format with no thousand separator`() {
        val formatter = DecimalFormatter(plainConfiguration)

        assertEquals("1234.56", formatter.format("123456"))
        assertEquals("1234567.89", formatter.format("123456789"))
        assertEquals("12.34", formatter.format("1234"))
    }

    // ===== Whole Numbers =====

    @Test
    fun `format whole numbers with no decimal places`() {
        val formatter = DecimalFormatter(wholeNumberConfiguration)

        assertEquals("1,234", formatter.format("1234"))
        assertEquals("123,456", formatter.format("123456"))
        assertEquals("1,234,567", formatter.format("1234567"))
        assertEquals("0", formatter.format(""))
        assertEquals("0", formatter.format("0"))
    }

    // ===== Different Decimal Places =====

    @Test
    fun `format with 3 decimal places`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 3,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 12
        )
        val formatter = DecimalFormatter(config)

        assertEquals("1,234.567", formatter.format("1234567"))
        assertEquals("12.345", formatter.format("12345"))
        assertEquals("0.123", formatter.format("123"))
        assertEquals("0.012", formatter.format("12"))
        assertEquals("0.001", formatter.format("1"))
    }

    @Test
    fun `format with 1 decimal place`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 1,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 12
        )
        val formatter = DecimalFormatter(config)

        assertEquals("1,234.5", formatter.format("12345"))
        assertEquals("123.4", formatter.format("1234"))
        assertEquals("0.1", formatter.format("1"))
    }

    // ===== Raw Digits Extraction =====

    @Test
    fun `getRawDigits extracts only digits`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("123456", formatter.getRawDigits("123456"))
        assertEquals("123456", formatter.getRawDigits("1,234.56"))
        assertEquals("123456", formatter.getRawDigits("$1,234.56"))
        assertEquals("123456", formatter.getRawDigits("1a2b3c4d5e6f"))
        assertEquals("123456", formatter.getRawDigits("   123   456   "))
    }

    @Test
    fun `getRawDigits handles empty and invalid input`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("", formatter.getRawDigits(""))
        assertEquals("", formatter.getRawDigits("abc"))
        assertEquals("", formatter.getRawDigits("$#@!"))
        assertEquals("123", formatter.getRawDigits("abc123def"))
    }

    @Test
    fun `getRawDigits removes leading zeros`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("123", formatter.getRawDigits("000123"))
        assertEquals("", formatter.getRawDigits("000"))
        assertEquals("", formatter.getRawDigits("0"))
    }

    @Test
    fun `getRawDigits respects maxDigits limit`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 2,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 6
        )
        val formatter = DecimalFormatter(config)

        assertEquals("123456", formatter.getRawDigits("12345678901234"))
    }

    // ===== Integration Tests =====

    @Test
    fun `complete format and extract cycle`() {
        val formatter = DecimalFormatter(usConfiguration)

        val originalDigits = "123456"
        val formatted = formatter.format(originalDigits)
        val extractedDigits = formatter.getRawDigits(formatted)

        assertEquals("1,234.56", formatted)
        assertEquals("123456", extractedDigits)
        assertEquals(originalDigits, extractedDigits)
    }

    @Test
    fun `format various currency-like inputs`() {
        val formatter = DecimalFormatter(usConfiguration)

        assertEquals("1,234.56", formatter.format(formatter.getRawDigits("$1,234.56")))
        assertEquals("1,234.56", formatter.format(formatter.getRawDigits("USD 1,234.56")))
        assertEquals("1,234.56", formatter.format(formatter.getRawDigits("€1,234.56")))
        assertEquals("1,234.56", formatter.format(formatter.getRawDigits("1.234,56 CHF")))
    }

    // ===== Property-Based Test Examples =====

    @Test
    fun `formatting preserves numeric value through round trip`() {
        val formatter = DecimalFormatter(usConfiguration)
        val testValues = listOf("0", "1", "12", "123", "1234", "12345", "123456", "1234567", "12345678")

        testValues.forEach { originalDigits ->
            val formatted = formatter.format(originalDigits)
            val extracted = formatter.getRawDigits(formatted)

            // After formatting and extracting, we should get back the original (cleaned) digits
            assertEquals(
                originalDigits.trimStart('0').ifEmpty { "" },
                extracted,
                "Round trip failed for input: $originalDigits"
            )
        }
    }

    // ===== Performance/Stress Tests =====

    @Test
    fun `format handles maximum length input`() {
        val formatter = DecimalFormatter(usConfiguration)
        val maxInput = "1".repeat(12) // Based on maxDigits = 12

        val result = formatter.format(maxInput)
        assertTrue(result.isNotEmpty())
        assertTrue(result.contains(",") || result.contains("."))
    }

    // ===== Boundary Tests =====

    @Test
    fun `format boundary cases for decimal places`() {
        val formatter = DecimalFormatter(usConfiguration)

        // Exactly matches decimal places
        assertEquals("12.34", formatter.format("1234"))

        // One less than decimal places
        assertEquals("0.12", formatter.format("12"))

        // One more than decimal places
        assertEquals("123.45", formatter.format("12345"))
    }
}