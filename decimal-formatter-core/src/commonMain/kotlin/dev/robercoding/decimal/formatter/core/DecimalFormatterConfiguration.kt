package dev.robercoding.decimal.formatter.core

import dev.robercoding.decimal.formatter.model.DecimalSeparator
import dev.robercoding.decimal.formatter.model.ThousandSeparator
import dev.robercoding.decimal.formatter.utils.logMessage

/**
 * Configuration for decimal number formatting.
 *
 * @property decimalSeparator The character used to separate decimals
 * @property thousandSeparator The character used to separate thousands
 * @property decimalPlaces The number of decimal places to display
 * @property maxDigits Maximum number of digits allowed
 */
data class DecimalFormatterConfiguration(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.COMMA,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.DOT,
    val decimalPlaces: Int = 2,
    val maxDigits: Int = 10
) {
    init {
        logMessage("Current configuration: $this")
        require(maxDigits > 0) { "Max digits must be higher than 0" }
        require(decimalPlaces >= 0) { "Decimal places must be non-negative" }
        require(decimalPlaces < maxDigits) { "Decimal places must be less than max digits" }
        require(
            thousandSeparator == ThousandSeparator.NONE ||
                thousandSeparator.char != decimalSeparator.char
        ) {
            "Thousand separator and decimal separator must be different"
        }
    }

    companion object {
        public val DefaultConfiguration
            get() = DecimalFormatterConfiguration()

        /**
         * European format: 1.234,56 (dot for thousands, comma for decimals)
         */
        fun european(decimalPlaces: Int = 2, maxDigits: Int = 10) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits
        )

        /**
         * US format: 1,234.56 (comma for thousands, dot for decimals)
         */
        fun us(decimalPlaces: Int = 2, maxDigits: Int = 10) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits
        )

        /**
         * Swiss format: 1'234.56 (apostrophe for thousands, dot for decimals)
         */
        fun swiss(decimalPlaces: Int = 2, maxDigits: Int = 10) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.APOSTROPHE,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits
        )

        /**
         * No separators: 1234.56
         */
        fun plain(decimalPlaces: Int = 2, maxDigits: Int = 10) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.NONE,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits
        )
    }
}