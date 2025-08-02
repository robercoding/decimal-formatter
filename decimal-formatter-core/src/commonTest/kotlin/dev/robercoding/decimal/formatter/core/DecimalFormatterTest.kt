package dev.robercoding.decimal.formatter.core

import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalSeparator
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class DecimalFormatterTest {

    private val usConfig = DecimalFormatterConfiguration.us()

    private val europeanConfig = DecimalFormatterConfiguration.european()

    private val swissConfig = DecimalFormatterConfiguration.swiss()

    private val plainConfig = DecimalFormatterConfiguration.plain()

    private val wholeNumberConfiguration = DecimalFormatterConfiguration(
        decimalPlaces = 0,
        thousandSeparator = ThousandSeparator.COMMA,
        decimalSeparator = DecimalSeparator.DOT,
        maxDigits = 12
    )

    // ===== Basic Formatting Tests =====

    @Test
    fun `format basic number with US configuration`() {
        val formatter = DecimalFormatter(usConfig)

        // Raw digits
        val longFormatted = formatter.format("123456")
        assertEquals("1,234.56", longFormatted.displayValue)
        assertEquals("1234.56", longFormatted.parseableValue)

        val mediumFormatted = formatter.format("1234")
        assertEquals("12.34", mediumFormatted.displayValue)
        assertEquals("12.34", mediumFormatted.parseableValue)

        val shortFormatted = formatter.format("12")
        assertEquals("0.12", shortFormatted.displayValue)
        assertEquals("0.12", shortFormatted.parseableValue)

        val singleDigitFormatted = formatter.format("1")
        assertEquals("0.01", singleDigitFormatted.displayValue)
        assertEquals("0.01", singleDigitFormatted.parseableValue)

        val emptyFormattedDigits = formatter.format("")
        assertEquals("0.00", emptyFormattedDigits.displayValue)
        assertEquals("0.00", emptyFormattedDigits.parseableValue)
    }

    @Test
    fun `format basic number with European configuration`() {
        val formatter = DecimalFormatter(europeanConfig)

        val longFormatted = formatter.format("123456")
        assertEquals("1.234,56", longFormatted.displayValue)
        assertEquals("1234.56", longFormatted.parseableValue)

        val mediumFormatted = formatter.format("1234")
        assertEquals("12,34", mediumFormatted.displayValue)
        assertEquals("12.34", mediumFormatted.parseableValue)

        val shortFormatted = formatter.format("12")
        assertEquals("0,12", shortFormatted.displayValue)
        assertEquals("0.12", shortFormatted.parseableValue)

        val singleDigitFormatted = formatter.format("1")
        assertEquals("0,01", singleDigitFormatted.displayValue)
        assertEquals("0.01", singleDigitFormatted.parseableValue)

        val emptyFormattedDigits = formatter.format("")
        assertEquals("0,00", emptyFormattedDigits.displayValue)
        assertEquals("0.00", emptyFormattedDigits.parseableValue)
    }

    @Test
    fun `format basic number with Swiss configuration`() {
        val formatter = DecimalFormatter(swissConfig)

        val longFormatted = formatter.format("123456")
        assertEquals("1'234.56", longFormatted.displayValue)
        assertEquals("1234.56", longFormatted.parseableValue)

        val mediumFormatted = formatter.format("1234")
        assertEquals("12.34", mediumFormatted.displayValue)
        assertEquals("12.34", mediumFormatted.parseableValue)

        val shortFormatted = formatter.format("12")
        assertEquals("0.12", shortFormatted.displayValue)
        assertEquals("0.12", shortFormatted.parseableValue)

        val singleDigitFormatted = formatter.format("1")
        assertEquals("0.01", singleDigitFormatted.displayValue)
        assertEquals("0.01", singleDigitFormatted.parseableValue)

        val emptyFormattedDigits = formatter.format("")
        assertEquals("0.00", emptyFormattedDigits.displayValue)
        assertEquals("0.00", emptyFormattedDigits.parseableValue)
    }

    // ===== Edge Cases =====

    @Test
    fun `format empty string returns zero`() {
        val formatter = DecimalFormatter(usConfig)

        val result = formatter.format("")
        assertEquals("0.00", result.displayValue)
        assertEquals("0.00", result.parseableValue)
    }

    @Test
    fun `format zero returns formatted zero`() {
        val formatter = DecimalFormatter(usConfig)

        val result1 = formatter.format("0")
        assertEquals("0.00", result1.displayValue)
        assertEquals("0.00", result1.parseableValue)

        val result2 = formatter.format("00")
        assertEquals("0.00", result2.displayValue)
        assertEquals("0.00", result2.parseableValue)

        val result3 = formatter.format("000")
        assertEquals("0.00", result3.displayValue)
        assertEquals("0.00", result3.parseableValue)
    }

    @Test
    fun `format single digit pads with leading zero`() {
        val formatter = DecimalFormatter(usConfig)

        val result1 = formatter.format("1")
        assertEquals("0.01", result1.displayValue)
        assertEquals("0.01", result1.parseableValue)

        val result5 = formatter.format("5")
        assertEquals("0.05", result5.displayValue)
        assertEquals("0.05", result5.parseableValue)

        val result9 = formatter.format("9")
        assertEquals("0.09", result9.displayValue)
        assertEquals("0.09", result9.parseableValue)
    }

    @Test
    fun `format removes leading zeros correctly`() {
        val formatter = DecimalFormatter(usConfig)

        val longResult = formatter.format("00123456")
        assertEquals("1,234.56", longResult.displayValue)
        assertEquals("1234.56", longResult.parseableValue)

        val mediumResult = formatter.format("001234")
        assertEquals("12.34", mediumResult.displayValue)
        assertEquals("12.34", mediumResult.parseableValue)

        val shortResult = formatter.format("0012")
        assertEquals("0.12", shortResult.displayValue)
        assertEquals("0.12", shortResult.parseableValue)
    }

    // ===== Large Numbers =====

    @Test
    fun `format large numbers with multiple thousand separators`() {
        val formatter = DecimalFormatter(usConfig.copy(maxDigits = 12))

        val result1 = formatter.format("123456789")
        assertEquals("1,234,567.89", result1.displayValue)
        assertEquals("1234567.89", result1.parseableValue)

        val result2 = formatter.format("1234567890")
        assertEquals("12,345,678.90", result2.displayValue)
        assertEquals("12345678.90", result2.parseableValue)

        val result3 = formatter.format("12345678901")
        assertEquals("123,456,789.01", result3.displayValue)
        assertEquals("123456789.01", result3.parseableValue)
    }

    @Test
    fun `format very large numbers`() {
        val expectedDisplayValue = "1,234,567,890.12"
        val expectedParseableValue = "1234567890.12"
        val formatter = DecimalFormatter(usConfig.copy(maxDigits = expectedDisplayValue.length))

        val result = formatter.format("123456789012")
        assertEquals(expectedDisplayValue, result.displayValue)
        assertEquals(expectedParseableValue, result.parseableValue)
    }

    @Test
    fun `format with no thousand separator`() {
        val formatter = DecimalFormatter(plainConfig)

        val result1 = formatter.format("123456")
        assertEquals("1234.56", result1.displayValue)
        assertEquals("1234.56", result1.parseableValue)

        val result2 = formatter.format("123456789")
        assertEquals("1234567.89", result2.displayValue)
        assertEquals("1234567.89", result2.parseableValue)

        val result3 = formatter.format("1234")
        assertEquals("12.34", result3.displayValue)
        assertEquals("12.34", result3.parseableValue)
    }

    // ===== Whole Numbers =====

    @Test
    fun `format whole numbers with no decimal places`() {
        val formatter = DecimalFormatter(wholeNumberConfiguration)

        val result1 = formatter.format("1234")
        assertEquals("1,234", result1.displayValue)
        assertEquals("1234", result1.parseableValue)

        val result2 = formatter.format("123456")
        assertEquals("123,456", result2.displayValue)
        assertEquals("123456", result2.parseableValue)

        val result3 = formatter.format("1234567")
        assertEquals("1,234,567", result3.displayValue)
        assertEquals("1234567", result3.parseableValue)

        val result5 = formatter.format("0")
        assertEquals("0", result5.displayValue)
        assertEquals("0", result5.parseableValue)

        val result4 = formatter.format("")
        assertEquals("0", result4.displayValue)
        assertEquals("0", result4.parseableValue)
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

        val result1 = formatter.format("1234567")
        assertEquals("1,234.567", result1.displayValue)
        assertEquals("1234.567", result1.parseableValue)

        val result2 = formatter.format("12345")
        assertEquals("12.345", result2.displayValue)
        assertEquals("12.345", result2.parseableValue)

        val result3 = formatter.format("123")
        assertEquals("0.123", result3.displayValue)
        assertEquals("0.123", result3.parseableValue)

        val result4 = formatter.format("12")
        assertEquals("0.012", result4.displayValue)
        assertEquals("0.012", result4.parseableValue)

        val result5 = formatter.format("1")
        assertEquals("0.001", result5.displayValue)
        assertEquals("0.001", result5.parseableValue)
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

        val result1 = formatter.format("12345")
        assertEquals("1,234.5", result1.displayValue)
        assertEquals("1234.5", result1.parseableValue)

        val result2 = formatter.format("1234")
        assertEquals("123.4", result2.displayValue)
        assertEquals("123.4", result2.parseableValue)

        val result3 = formatter.format("1")
        assertEquals("0.1", result3.displayValue)
        assertEquals("0.1", result3.parseableValue)
    }

    // ===== Performance/Stress Tests =====

    @Test
    fun `format handles maximum length input`() {
        val formatter = DecimalFormatter(usConfig)
        val maxInput = "1".repeat(12) // Based on maxDigits = 12

        val result = formatter.format(maxInput)
        val displayValue = result.displayValue
        assertTrue(displayValue.isNotEmpty())
        assertTrue(displayValue.contains(",") || displayValue.contains("."))

        val parseableValue = result.parseableValue
        assertTrue(parseableValue.isNotEmpty())
        assertTrue(parseableValue.contains(".") && !parseableValue.contains(","))
    }

    // ===== Boundary Tests =====

    @Test
    fun `format boundary cases for decimal places`() {
        val formatter = DecimalFormatter(usConfig)

        // Exactly matches decimal places
        val exactResult = formatter.format("1234")
        assertEquals("12.34", exactResult.displayValue)
        assertEquals("12.34", exactResult.parseableValue)

        // One less than decimal places
        val lessResult = formatter.format("12")
        assertEquals("0.12", lessResult.displayValue)
        assertEquals("0.12", lessResult.parseableValue)

        // One more than decimal places
        val moreResult = formatter.format("12345")
        assertEquals("123.45", moreResult.displayValue)
        assertEquals("123.45", moreResult.parseableValue)
    }

    @Test
    fun `configuration factories return expected settings including prefix suffix`() {
        assertEquals(ThousandSeparator.COMMA, usConfig.thousandSeparator)
        assertEquals(DecimalSeparator.DOT, usConfig.decimalSeparator)
        assertEquals(2, usConfig.decimalPlaces)
        assertEquals("", usConfig.prefix)
        assertEquals("", usConfig.suffix)

        assertEquals(ThousandSeparator.DOT, europeanConfig.thousandSeparator)
        assertEquals(DecimalSeparator.COMMA, europeanConfig.decimalSeparator)
        assertEquals(2, europeanConfig.decimalPlaces)
        assertEquals("", europeanConfig.prefix)
        assertEquals("", europeanConfig.suffix)

        assertEquals(ThousandSeparator.APOSTROPHE, swissConfig.thousandSeparator)
        assertEquals(DecimalSeparator.DOT, swissConfig.decimalSeparator)
        assertEquals(2, swissConfig.decimalPlaces)
        assertEquals("", swissConfig.prefix)
        assertEquals("", swissConfig.suffix)

        assertEquals(ThousandSeparator.NONE, plainConfig.thousandSeparator)
        assertEquals(DecimalSeparator.DOT, plainConfig.decimalSeparator)
        assertEquals("", plainConfig.prefix)
        assertEquals("", plainConfig.suffix)

        // Test new currency configurations
        val usdConfig = DecimalFormatterConfiguration.usd()
        assertEquals("$", usdConfig.prefix)
        assertEquals("", usdConfig.suffix)

        val euroConfig = DecimalFormatterConfiguration.euro()
        assertEquals("", euroConfig.prefix)
        assertEquals(" €", euroConfig.suffix)

        val percentConfig = DecimalFormatterConfiguration.percentage()
        assertEquals("", percentConfig.prefix)
        assertEquals("%", percentConfig.suffix)
    }

    // ===== MaxDigits Boundary Tests =====

    @Test
    fun `format respects maxDigits limit`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 2,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 6
        )
        val formatter = DecimalFormatter(config)

        // Input longer than maxDigits should be truncated
        val result = formatter.format("12345678901234")
        // Should only take first 6 digits: "123456" -> "1234.56"
        assertEquals("1,234.56", result.displayValue)
        assertEquals("1234.56", result.parseableValue)
    }

    @Test
    fun `format with maxDigits close to decimalPlaces`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 2,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 3
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("12")
        assertEquals("0.12", result1.displayValue)
        assertEquals("0.12", result1.parseableValue)

        val result2 = formatter.format("123")
        assertEquals("1.23", result2.displayValue) // Takes first 3 digits: "123" -> "1.23"
        assertEquals("1.23", result2.parseableValue)

        val result3 = formatter.format("123456")
        assertEquals("1.23", result3.displayValue) // Should only take first 3 digits
        assertEquals("1.23", result3.parseableValue)
    }

    // ===== Consistency Tests =====

    @Test
    fun `parseableValue is always in standard format regardless of display locale`() {
        val testInput = "123456789"

        val usResult = DecimalFormatter(usConfig).format(testInput)
        val europeanResult = DecimalFormatter(europeanConfig).format(testInput)
        val swissResult = DecimalFormatter(swissConfig).format(testInput)

        // Display values should be different
        assertEquals("1,234,567.89", usResult.displayValue)
        assertEquals("1.234.567,89", europeanResult.displayValue)
        assertEquals("1'234'567.89", swissResult.displayValue)

        // But parseableValue should be identical (standard format)
        val expectedParseableValue = "1234567.89"
        assertEquals(expectedParseableValue, usResult.parseableValue)
        assertEquals(expectedParseableValue, europeanResult.parseableValue)
        assertEquals(expectedParseableValue, swissResult.parseableValue)
    }

    @Test
    fun `parseableValue never contains thousand separators`() {
        val testCases = listOf(
            usConfig to "1,234,567.89",
            europeanConfig to "1.234.567,89",
            swissConfig to "1'234'567.89"
        )

        testCases.forEach { (config, expectedDisplay) ->
            val result = DecimalFormatter(config).format("123456789")
            assertEquals(expectedDisplay, result.displayValue)

            // parseableValue should never contain any thousand separators
            assertFalse(result.parseableValue.contains(","))
            assertFalse(result.parseableValue.contains("'"))

            // European uses dot as thousand separator, but parseableValue uses dot as decimal
            assertTrue(result.parseableValue.count { it == '.' } <= 1) // At most one dot (decimal)
        }
    }

    // ===== Result Object Validation =====

    @Test
    fun `format always returns both displayValue and parseableValue`() {
        val formatter = DecimalFormatter(usConfig)
        val testInputs = listOf("", "0", "1", "123", "123456789", "00001234")

        testInputs.forEach { input ->
            val result = formatter.format(input)

            assertNotNull(result.displayValue, "displayValue should never be null for input: $input")
            assertNotNull(result.parseableValue, "parseableValue should never be null for input: $input")
            assertTrue(result.displayValue.isNotEmpty(), "displayValue should not be empty for input: $input")
            assertTrue(result.parseableValue.isNotEmpty(), "parseableValue should not be empty for input: $input")
        }
    }

    // ===== Decimal Places Edge Cases =====

    @Test
    fun `format with 0 decimal places handles fractional input correctly`() {
        val formatter = DecimalFormatter(wholeNumberConfiguration)

        // These inputs would normally have decimal places, but should be truncated
        val result1 = formatter.format("12345") // Would be 123.45 with 2 decimal places
        assertEquals("12,345", result1.displayValue) // Should treat as whole number 12345
        assertEquals("12345", result1.parseableValue)

        val result2 = formatter.format("1") // Would be 0.01 with 2 decimal places
        assertEquals("1", result2.displayValue) // Should treat as whole number 1
        assertEquals("1", result2.parseableValue)
    }

    // ===== Large Number Edge Cases =====

    @Test
    fun `format extremely large numbers within maxDigits`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 2,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 15
        )
        val formatter = DecimalFormatter(config)

        val result = formatter.format("999999999999999") // 15 digits
        assertEquals("9,999,999,999,999.99", result.displayValue)
        assertEquals("9999999999999.99", result.parseableValue)
    }

    // ===== Input Sanitization Tests =====

    @Test
    fun `format handles non-digit characters gracefully`() {
        val formatter = DecimalFormatter(usConfig)

        val result1 = formatter.format("1a2b3c/4#5@6")
        assertEquals("1,234.56", result1.displayValue) // Extracts "123456" -> "1234.56"
        assertEquals("1234.56", result1.parseableValue)

        val result2 = formatter.format("abc")
        assertEquals("0.00", result2.displayValue) // Defaults to zero for non-digit input
        assertEquals("0.00", result2.parseableValue)

        val result3 = formatter.format("12.34") // Already formatted input
        assertEquals("12.34", result3.displayValue) // Extracts "1234" -> "12.34"
        assertEquals("12.34", result3.parseableValue)

        val result4 = formatter.format("$1,234.56")
        assertEquals("1,234.56", result4.displayValue) // Extracts "123456" -> "1234.56"
        assertEquals("1234.56", result4.parseableValue)

        val result5 = formatter.format("   ")
        assertEquals("0.00", result5.displayValue) // Whitespace treated as empty
        assertEquals("0.00", result5.parseableValue)
    }

    // ===== Prefix/Suffix Tests =====

    @Test
    fun `format with prefix only`() {
        val config = DecimalFormatterConfiguration.us(prefix = "$")
        val formatter = DecimalFormatter(config)

        val result = formatter.format("123456")
        assertEquals("$1,234.56", result.displayValue)
        assertEquals("1234.56", result.parseableValue) // parseableValue should never include prefix
    }

    @Test
    fun `format with suffix only`() {
        val config = DecimalFormatterConfiguration.us(suffix = "%")
        val formatter = DecimalFormatter(config)

        val result = formatter.format("1234")
        assertEquals("12.34%", result.displayValue)
        assertEquals("12.34", result.parseableValue) // parseableValue should never include suffix
    }

    @Test
    fun `format with both prefix and suffix`() {
        val config = DecimalFormatterConfiguration.us(prefix = "USD ", suffix = " total")
        val formatter = DecimalFormatter(config)

        val result = formatter.format("123456")
        assertEquals("USD 1,234.56 total", result.displayValue)
        assertEquals("1234.56", result.parseableValue) // parseableValue should be clean
    }

    @Test
    fun `format with empty input and prefix suffix`() {
        val config = DecimalFormatterConfiguration.us(prefix = "$", suffix = " USD")
        val formatter = DecimalFormatter(config)

        val result = formatter.format("")
        assertEquals("$0.00 USD", result.displayValue)
        assertEquals("0.00", result.parseableValue)
    }

    // ===== Currency Factory Method Tests =====

    @Test
    fun `usd configuration formats correctly`() {
        val formatter = DecimalFormatter(DecimalFormatterConfiguration.usd())

        val result = formatter.format("123456")
        assertEquals("$1,234.56", result.displayValue)
        assertEquals("1234.56", result.parseableValue)

        // Verify configuration
        val config = DecimalFormatterConfiguration.usd()
        assertEquals("$", config.prefix)
        assertEquals("", config.suffix)
        assertEquals(ThousandSeparator.COMMA, config.thousandSeparator)
        assertEquals(DecimalSeparator.DOT, config.decimalSeparator)
    }

    @Test
    fun `euro configuration formats correctly`() {
        val formatter = DecimalFormatter(DecimalFormatterConfiguration.euro())

        val result = formatter.format("123456")
        assertEquals("1.234,56 €", result.displayValue)
        assertEquals("1234.56", result.parseableValue)

        // Verify configuration
        val config = DecimalFormatterConfiguration.euro()
        assertEquals("", config.prefix)
        assertEquals(" €", config.suffix)
        assertEquals(ThousandSeparator.DOT, config.thousandSeparator)
        assertEquals(DecimalSeparator.COMMA, config.decimalSeparator)
    }

    @Test
    fun `percentage configuration formats correctly`() {
        val formatter = DecimalFormatter(DecimalFormatterConfiguration.percentage())

        val result = formatter.format("1234")
        assertEquals("12.34%", result.displayValue)
        assertEquals("12.34", result.parseableValue)

        // Verify configuration
        val config = DecimalFormatterConfiguration.percentage()
        assertEquals("", config.prefix)
        assertEquals("%", config.suffix)
        assertEquals(ThousandSeparator.COMMA, config.thousandSeparator)
        assertEquals(DecimalSeparator.DOT, config.decimalSeparator)
    }

    // ===== Prefix/Suffix with Different Locales =====

    @Test
    fun `prefix and suffix work with all locale configurations`() {
        val testInput = "123456"
        val expectedParseableValue = "1234.56"

        // US with prefix/suffix
        val usResult = DecimalFormatter(
            DecimalFormatterConfiguration.us(prefix = "$", suffix = " USD")
        ).format(testInput)
        assertEquals("$1,234.56 USD", usResult.displayValue)
        assertEquals(expectedParseableValue, usResult.parseableValue)

        // European with prefix/suffix
        val euroResult = DecimalFormatter(
            DecimalFormatterConfiguration.european(prefix = "€", suffix = " EUR")
        ).format(testInput)
        assertEquals("€1.234,56 EUR", euroResult.displayValue)
        assertEquals(expectedParseableValue, euroResult.parseableValue)

        // Swiss with prefix/suffix
        val swissResult = DecimalFormatter(
            DecimalFormatterConfiguration.swiss(prefix = "CHF ", suffix = " total")
        ).format(testInput)
        assertEquals("CHF 1'234.56 total", swissResult.displayValue)
        assertEquals(expectedParseableValue, swissResult.parseableValue)
    }

    // ===== Whole Numbers with Prefix/Suffix =====

    @Test
    fun `prefix and suffix work with whole numbers`() {
        val config = DecimalFormatterConfiguration(
            decimalPlaces = 0,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalSeparator = DecimalSeparator.DOT,
            maxDigits = 12,
            prefix = "Count: ",
            suffix = " items"
        )
        val formatter = DecimalFormatter(config)

        val result = formatter.format("123456")
        assertEquals("Count: 123,456 items", result.displayValue)
        assertEquals("123456", result.parseableValue) // No prefix/suffix in parseable
    }

    // ===== Edge Cases with Prefix/Suffix =====

    @Test
    fun `special characters in prefix and suffix`() {
        val config = DecimalFormatterConfiguration.us(
            prefix = "[$",
            suffix = "] (tax incl.)"
        )
        val formatter = DecimalFormatter(config)

        val result = formatter.format("123456")
        assertEquals("[$1,234.56] (tax incl.)", result.displayValue)
        assertEquals("1234.56", result.parseableValue)
    }

    @Test
    fun `parseableValue consistency with prefix suffix across all scenarios`() {
        val testInput = "12345"
        val expectedParseableValue = "123.45"

        val testConfigs = listOf(
            DecimalFormatterConfiguration.us(),
            DecimalFormatterConfiguration.us(prefix = "$"),
            DecimalFormatterConfiguration.us(suffix = " USD"),
            DecimalFormatterConfiguration.us(prefix = "$", suffix = " USD"),
            DecimalFormatterConfiguration.usd(),
            DecimalFormatterConfiguration.euro(),
            DecimalFormatterConfiguration.percentage(),
        )

        testConfigs.forEach { config ->
            val result = DecimalFormatter(config).format(testInput)
            assertEquals(
                expectedParseableValue,
                result.parseableValue,
                "parseableValue should be consistent regardless of prefix/suffix for config: $config"
            )
            // parseableValue should never contain prefix/suffix characters
            assertFalse(result.parseableValue.contains("$"))
            assertFalse(result.parseableValue.contains("€"))
            assertFalse(result.parseableValue.contains("%"))
            assertFalse(result.parseableValue.contains("USD"))
            assertFalse(result.parseableValue.contains("EUR"))
        }
    }

    // ===== Input Sanitization with Prefix/Suffix =====

    @Test
    fun `input sanitization works correctly with prefix and suffix`() {
        val formatter = DecimalFormatter(DecimalFormatterConfiguration.usd())

        // Should extract digits and apply formatting with prefix
        val result1 = formatter.format("1a2b3c")
        assertEquals("$1.23", result1.displayValue)
        assertEquals("1.23", result1.parseableValue)

        // Should handle non-digit input with prefix
        val result2 = formatter.format("abc")
        assertEquals("$0.00", result2.displayValue)
        assertEquals("0.00", result2.parseableValue)

        // Should extract from currency-formatted input
        val result3 = formatter.format("€1,234.56")
        assertEquals("$1,234.56", result3.displayValue) // Extracts digits, applies USD formatting
        assertEquals("1234.56", result3.parseableValue)
    }
}