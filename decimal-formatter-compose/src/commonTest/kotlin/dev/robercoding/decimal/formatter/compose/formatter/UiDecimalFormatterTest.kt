package dev.robercoding.decimal.formatter.compose.formatter

import dev.robercoding.decimal.formatter.core.DecimalFormatter
import dev.robercoding.decimal.formatter.core.DecimalFormatterConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UiDecimalFormatterTest {

    private val europeanConfiguration = DecimalFormatterConfiguration.european()
    private val europeanFormatter = DecimalFormatter(europeanConfiguration)

    // ===== Basic Prefix Tests =====

    @Test
    fun formatWithNoPrefixReturnsNullFullDisplay() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = null)

        val result = uiFormatter.format("123456")

        assertEquals("123456", result.rawDigits)
        assertEquals("1.234,56", result.display)
        assertNull(result.fullDisplay)
    }

    @Test
    fun formatWithEmptyPrefixReturnsNullFullDisplay() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "")

        val result = uiFormatter.format("123456")

        assertEquals("123456", result.rawDigits)
        assertEquals("1.234,56", result.display)
        assertNull(result.fullDisplay)
    }

    @Test
    fun formatWithBlankPrefixReturnsNullFullDisplay() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "   ")

        val result = uiFormatter.format("123456")

        assertEquals("123456", result.rawDigits)
        assertEquals("1.234,56", result.display)
        assertNull(result.fullDisplay)
    }

    @Test
    fun formatWithValidPrefixReturnsFullDisplay() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "€")

        val result = uiFormatter.format("123456")

        assertEquals("123456", result.rawDigits)
        assertEquals("1.234,56", result.display)
        assertEquals("€1.234,56", result.fullDisplay)
    }

    @Test
    fun formatWithPrefixContainingSpaces() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "€ ")

        val result = uiFormatter.format("123456")

        assertEquals("123456", result.rawDigits)
        assertEquals("1.234,56", result.display)
        assertEquals("€ 1.234,56", result.fullDisplay)
    }

    @Test
    fun formatWithMultiCharacterPrefix() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "EUR ")

        val result = uiFormatter.format("123456")

        assertEquals("123456", result.rawDigits)
        assertEquals("1.234,56", result.display)
        assertEquals("EUR 1.234,56", result.fullDisplay)
    }

    @Test
    fun formatZeroValueWithPrefix() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "€")

        val result = uiFormatter.format("0")

        assertEquals("", result.rawDigits)
        assertEquals("0,00", result.display)
        assertEquals("€0,00", result.fullDisplay)
    }

    @Test
    fun formatEmptyStringWithPrefix() {
        val uiFormatter = UiDecimalFormatter(europeanFormatter, prefix = "€")

        val result = uiFormatter.format("")

        assertEquals("", result.rawDigits)
        assertEquals("0,00", result.display)
        assertEquals("€0,00", result.fullDisplay)
    }
}