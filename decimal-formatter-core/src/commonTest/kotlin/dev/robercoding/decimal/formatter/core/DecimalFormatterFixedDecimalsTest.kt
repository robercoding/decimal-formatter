package dev.robercoding.decimal.formatter.core

import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalInputMode
import dev.robercoding.decimal.formatter.core.model.DecimalSeparator
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Comprehensive tests for FIXED_DECIMALS input mode.
 *
 * FIXED_DECIMALS mode treats input as complete numbers with fixed decimal places:
 * - Raw digits are treated as whole numbers
 * - Decimal places are padded with zeros
 * - Supports parsing formatted input with decimal separators (. or ,)
 */
class DecimalFormatterFixedDecimalsTest {

    // ===== Basic Fixed Decimals Tests (User's Examples) =====

    @Test
    fun `FIXED_DECIMALS - user examples with 3 decimal places`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 3,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        // "1" → "1.000"
        val result1 = formatter.format("1")
        assertEquals("1.000", result1.displayValue)
        assertEquals("1.000", result1.parseableValue)

        // "14" → "14.000"
        val result2 = formatter.format("14")
        assertEquals("14.000", result2.displayValue)
        assertEquals("14.000", result2.parseableValue)

        // "1.45" → "1.450"
        val result3 = formatter.format("1.45")
        assertEquals("1.450", result3.displayValue)
        assertEquals("1.450", result3.parseableValue)

        // "14.50" → "14.500"
        val result4 = formatter.format("14.50")
        assertEquals("14.500", result4.displayValue)
        assertEquals("14.500", result4.parseableValue)

        // "14.500" → "14.500"
        val result5 = formatter.format("14.500")
        assertEquals("14.500", result5.displayValue)
        assertEquals("14.500", result5.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - raw digits without separator pad with zeros`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1")
        assertEquals("1.00", result1.displayValue)
        assertEquals("1.00", result1.parseableValue)

        val result2 = formatter.format("123")
        assertEquals("123.00", result2.displayValue)
        assertEquals("123.00", result2.parseableValue)

        val result3 = formatter.format("1234567")
        assertEquals("1,234,567.00", result3.displayValue)
        assertEquals("1234567.00", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - formatted input with dot separator`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1.5")
        assertEquals("1.50", result1.displayValue)
        assertEquals("1.50", result1.parseableValue)

        val result2 = formatter.format("123.4")
        assertEquals("123.40", result2.displayValue)
        assertEquals("123.40", result2.parseableValue)

        val result3 = formatter.format("123.45")
        assertEquals("123.45", result3.displayValue)
        assertEquals("123.45", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - formatted input with comma separator`() {
        val config = DecimalFormatterConfiguration.european(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1,5")
        assertEquals("1,50", result1.displayValue)
        assertEquals("1.50", result1.parseableValue)

        val result2 = formatter.format("123,4")
        assertEquals("123,40", result2.displayValue)
        assertEquals("123.40", result2.parseableValue)

        val result3 = formatter.format("123,45")
        assertEquals("123,45", result3.displayValue)
        assertEquals("123.45", result3.parseableValue)
    }

    // ===== Edge Cases =====

    @Test
    fun `FIXED_DECIMALS - empty input returns zero with padding`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result = formatter.format("")
        assertEquals("0.00", result.displayValue)
        assertEquals("0.00", result.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - zero input with padding`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 3,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("0")
        assertEquals("0.000", result1.displayValue)
        assertEquals("0.000", result1.parseableValue)

        val result2 = formatter.format("0.0")
        assertEquals("0.000", result2.displayValue)
        assertEquals("0.000", result2.parseableValue)

        val result3 = formatter.format("0.00")
        assertEquals("0.000", result3.displayValue)
        assertEquals("0.000", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - leading zeros are trimmed from integer part`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("0001")
        assertEquals("1.00", result1.displayValue)
        assertEquals("1.00", result1.parseableValue)

        val result2 = formatter.format("00123")
        assertEquals("123.00", result2.displayValue)
        assertEquals("123.00", result2.parseableValue)

        val result3 = formatter.format("000.5")
        assertEquals("0.50", result3.displayValue)
        assertEquals("0.50", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - trailing zeros in input are preserved`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 4,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1.50")
        assertEquals("1.5000", result1.displayValue)
        assertEquals("1.5000", result1.parseableValue)

        val result2 = formatter.format("1.500")
        assertEquals("1.5000", result2.displayValue)
        assertEquals("1.5000", result2.parseableValue)

        val result3 = formatter.format("1.5000")
        assertEquals("1.5000", result3.displayValue)
        assertEquals("1.5000", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - decimal part longer than configured decimals is truncated`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1.123")
        assertEquals("1.12", result1.displayValue)
        assertEquals("1.12", result1.parseableValue)

        val result2 = formatter.format("123.456789")
        assertEquals("123.45", result2.displayValue)
        assertEquals("123.45", result2.parseableValue)
    }

    // ===== Different Decimal Places =====

    @Test
    fun `FIXED_DECIMALS - with 0 decimal places`() {
        val config = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = 0,
            maxDigits = 10,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("123")
        assertEquals("123", result1.displayValue)
        assertEquals("123", result1.parseableValue)

        val result2 = formatter.format("123.45")
        assertEquals("123", result2.displayValue) // Decimal part ignored
        assertEquals("123", result2.parseableValue)

        val result3 = formatter.format("1234567")
        assertEquals("1,234,567", result3.displayValue)
        assertEquals("1234567", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - with 1 decimal place`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 1,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1")
        assertEquals("1.0", result1.displayValue)
        assertEquals("1.0", result1.parseableValue)

        val result2 = formatter.format("1.5")
        assertEquals("1.5", result2.displayValue)
        assertEquals("1.5", result2.parseableValue)

        val result3 = formatter.format("1.56")
        assertEquals("1.5", result3.displayValue) // Truncated
        assertEquals("1.5", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - with 4 decimal places`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 4,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1")
        assertEquals("1.0000", result1.displayValue)
        assertEquals("1.0000", result1.parseableValue)

        val result2 = formatter.format("1.5")
        assertEquals("1.5000", result2.displayValue)
        assertEquals("1.5000", result2.parseableValue)

        val result3 = formatter.format("1.1234")
        assertEquals("1.1234", result3.displayValue)
        assertEquals("1.1234", result3.parseableValue)
    }

    // ===== Large Numbers =====

    @Test
    fun `FIXED_DECIMALS - large numbers with thousand separators`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            maxDigits = 12,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1234567")
        assertEquals("1,234,567.00", result1.displayValue)
        assertEquals("1234567.00", result1.parseableValue)

        val result2 = formatter.format("1234567.89")
        assertEquals("1,234,567.89", result2.displayValue)
        assertEquals("1234567.89", result2.parseableValue)

        val result3 = formatter.format("123456789")
        assertEquals("123,456,789.00", result3.displayValue)
        assertEquals("123456789.00", result3.parseableValue)
    }

    // ===== Different Locale Configurations =====

    @Test
    fun `FIXED_DECIMALS - European format with comma decimal separator`() {
        val config = DecimalFormatterConfiguration.european(
            decimalPlaces = 3,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("14")
        assertEquals("14,000", result1.displayValue)
        assertEquals("14.000", result1.parseableValue)

        val result2 = formatter.format("14.5") // Dot separator in input
        assertEquals("14,500", result2.displayValue)
        assertEquals("14.500", result2.parseableValue)

        val result3 = formatter.format("14,5") // Comma separator in input
        assertEquals("14,500", result3.displayValue)
        assertEquals("14.500", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - Swiss format`() {
        val config = DecimalFormatterConfiguration.swiss(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1234")
        assertEquals("1'234.00", result1.displayValue)
        assertEquals("1234.00", result1.parseableValue)

        val result2 = formatter.format("1234.5")
        assertEquals("1'234.50", result2.displayValue)
        assertEquals("1234.50", result2.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - plain format without thousand separators`() {
        val config = DecimalFormatterConfiguration.plain(
            decimalPlaces = 3,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("1234567")
        assertEquals("1234567.000", result1.displayValue)
        assertEquals("1234567.000", result1.parseableValue)

        val result2 = formatter.format("1234.5")
        assertEquals("1234.500", result2.displayValue)
        assertEquals("1234.500", result2.parseableValue)
    }

    // ===== With Prefix and Suffix =====

    @Test
    fun `FIXED_DECIMALS - USD format with prefix`() {
        val config = DecimalFormatterConfiguration.usd(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("100")
        assertEquals("$100.00", result1.displayValue)
        assertEquals("100.00", result1.parseableValue)

        val result2 = formatter.format("100.5")
        assertEquals("$100.50", result2.displayValue)
        assertEquals("100.50", result2.parseableValue)

        val result3 = formatter.format("1234.56")
        assertEquals("$1,234.56", result3.displayValue)
        assertEquals("1234.56", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - Euro format with suffix`() {
        val config = DecimalFormatterConfiguration.euro(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("100")
        assertEquals("100,00 €", result1.displayValue)
        assertEquals("100.00", result1.parseableValue)

        val result2 = formatter.format("100.5")
        assertEquals("100,50 €", result2.displayValue)
        assertEquals("100.50", result2.parseableValue)

        val result3 = formatter.format("1234,56")
        assertEquals("1.234,56 €", result3.displayValue)
        assertEquals("1234.56", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - custom suffix for weight`() {
        val config = DecimalFormatterConfiguration.plain(
            decimalPlaces = 2,
            suffix = " kg",
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("123")
        assertEquals("123.00 kg", result1.displayValue)
        assertEquals("123.00", result1.parseableValue)

        val result2 = formatter.format("123.45")
        assertEquals("123.45 kg", result2.displayValue)
        assertEquals("123.45", result2.parseableValue)
    }

    // ===== Input Sanitization =====

    @Test
    fun `FIXED_DECIMALS - handles messy input with non-digit characters`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format("$123.45")
        assertEquals("123.45", result1.displayValue)
        assertEquals("123.45", result1.parseableValue)

        val result2 = formatter.format("1234.56")
        assertEquals("1,234.56", result2.displayValue)
        assertEquals("1234.56", result2.parseableValue)

        val result3 = formatter.format("abc123def.45xyz")
        assertEquals("123.45", result3.displayValue)
        assertEquals("123.45", result3.parseableValue)
    }

    @Test
    fun `FIXED_DECIMALS - handles input with only decimal separator`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        val result1 = formatter.format(".")
        assertEquals("0.00", result1.displayValue)
        assertEquals("0.00", result1.parseableValue)

        val result2 = formatter.format(".5")
        assertEquals("0.50", result2.displayValue)
        assertEquals("0.50", result2.parseableValue)

        val result3 = formatter.format(".55")
        assertEquals("0.55", result3.displayValue)
        assertEquals("0.55", result3.parseableValue)
    }

    // ===== Comparison with FRACTIONAL Mode =====

    @Test
    fun `Compare FIXED_DECIMALS vs FRACTIONAL mode behavior`() {
        val fractionalConfig = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FRACTIONAL
        )
        val fixedConfig = DecimalFormatterConfiguration.us(
            decimalPlaces = 2,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )

        val fractionalFormatter = DecimalFormatter(fractionalConfig)
        val fixedFormatter = DecimalFormatter(fixedConfig)

        // Input "1" - different behavior
        val fractionalResult1 = fractionalFormatter.format("1")
        assertEquals("0.01", fractionalResult1.displayValue) // 1 cent

        val fixedResult1 = fixedFormatter.format("1")
        assertEquals("1.00", fixedResult1.displayValue) // 1 dollar

        // Input "123" - different behavior
        val fractionalResult2 = fractionalFormatter.format("123")
        assertEquals("1.23", fractionalResult2.displayValue) // $1.23

        val fixedResult2 = fixedFormatter.format("123")
        assertEquals("123.00", fixedResult2.displayValue) // $123.00
    }

    @Test
    fun `FIXED_DECIMALS mode preserves user intent with formatted input`() {
        val config = DecimalFormatterConfiguration.us(
            decimalPlaces = 3,
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        val formatter = DecimalFormatter(config)

        // User types "5" - means 5 units, not 0.005
        val result1 = formatter.format("5")
        assertEquals("5.000", result1.displayValue)

        // User types "5.5" - means 5.5 units
        val result2 = formatter.format("5.5")
        assertEquals("5.500", result2.displayValue)

        // User types "5.50" - preserve the precision
        val result3 = formatter.format("5.50")
        assertEquals("5.500", result3.displayValue)
    }

    // ===== Parseable Value Consistency =====

    @Test
    fun `FIXED_DECIMALS - parseableValue is always in standard format`() {
        val configs = listOf(
            DecimalFormatterConfiguration.us(inputMode = DecimalInputMode.FIXED_DECIMALS),
            DecimalFormatterConfiguration.european(inputMode = DecimalInputMode.FIXED_DECIMALS),
            DecimalFormatterConfiguration.swiss(inputMode = DecimalInputMode.FIXED_DECIMALS)
        )

        configs.forEach { config ->
            val formatter = DecimalFormatter(config)
            val result = formatter.format("1234.56")

            // Parseable value should always use dot separator and no thousand separators
            assertEquals("1234.56", result.parseableValue)
            assertEquals(1, result.parseableValue.count { it == '.' })
            assertEquals(0, result.parseableValue.count { it == ',' })
            assertEquals(0, result.parseableValue.count { it == '\'' })
        }
    }

    // ===== Factory Methods with inputMode =====

    @Test
    fun `Factory methods support inputMode parameter`() {
        val usdFixed = DecimalFormatterConfiguration.usd(
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        assertEquals(DecimalInputMode.FIXED_DECIMALS, usdFixed.inputMode)

        val euroFixed = DecimalFormatterConfiguration.euro(
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        assertEquals(DecimalInputMode.FIXED_DECIMALS, euroFixed.inputMode)

        val percentFixed = DecimalFormatterConfiguration.percentage(
            inputMode = DecimalInputMode.FIXED_DECIMALS
        )
        assertEquals(DecimalInputMode.FIXED_DECIMALS, percentFixed.inputMode)
    }

    @Test
    fun `Default inputMode is FRACTIONAL for backward compatibility`() {
        val defaultConfig = DecimalFormatterConfiguration.us()
        assertEquals(DecimalInputMode.FRACTIONAL, defaultConfig.inputMode)
    }
}
