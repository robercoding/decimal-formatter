package dev.robercoding.decimal.formatter.core.formatter

import dev.robercoding.decimal.formatter.core.model.DecimalSeparator
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator
import dev.robercoding.decimal.formatter.core.utils.logMessage

/**
 * Configuration for decimal number formatting.
 *
 * @property decimalSeparator The character used to separate decimals
 * @property thousandSeparator The character used to separate thousands
 * @property decimalPlaces The number of decimal places to display
 * @property maxDigits Maximum number of digits allowed
 * @property prefix String to prepend to the formatted number (e.g., "$")
 * @property suffix String to append to the formatted number (e.g., " USD")
 */
data class DecimalFormatterConfiguration(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.COMMA,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.DOT,
    val decimalPlaces: Int = 2,
    val maxDigits: Int = 10,
    val prefix: String = "",
    val suffix: String = ""
) {
    init {
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
        public val DefaultConfiguration by lazy { DecimalFormatterConfiguration() }

        /**
         * European format: 1.234,56 (dot for thousands, comma for decimals)
         */
        fun european(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = ""
        ) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix
        )

        /**
         * US format: 1,234.56 (comma for thousands, dot for decimals)
         */
        fun us(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = ""
        ) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix
        )

        /**
         * Swiss format: 1'234.56 (apostrophe for thousands, dot for decimals)
         */
        fun swiss(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = ""
        ) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.APOSTROPHE,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix
        )

        /**
         * No separators: 1234.56
         */
        fun plain(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10,
            prefix: String = "",
            suffix: String = ""
        ) = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.NONE,
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = prefix,
            suffix = suffix
        )

        /**
         * US Dollar format: $1,234.56
         */
        fun usd(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10
        ) = us(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = "$"
        )

        /**
         * Swiss Franc format: CHF 1'234.56
         */
        fun chf(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10
        ) = swiss(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            prefix = "CHF "
        )

        /**
         * Euro format: 1.234,56 €
         */
        fun euro(
            decimalPlaces: Int = 2,
            maxDigits: Int = 10
        ) = european(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            suffix = " €"
        )

        /**
         * Percentage format: 12.34%
         */
        fun percentage(
            decimalPlaces: Int = 2,
            maxDigits: Int = 5
        ) = us(
            decimalPlaces = decimalPlaces,
            maxDigits = maxDigits,
            suffix = "%"
        )
    }
}