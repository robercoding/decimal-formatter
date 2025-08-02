package dev.robercoding.decimal.formatter.core.formatter

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator
import dev.robercoding.decimal.formatter.core.utils.logMessage

/**
 * A formatter for decimal numbers with configurable separators and decimal places.
 */
data class DecimalFormatter(private val configuration: DecimalFormatterConfiguration) {

    companion object {
        public val DefaultFormatter: DecimalFormatter
            get() = DecimalFormatter(DecimalFormatterConfiguration.us())
    }

    public fun createEmptyValue(): FormattedDecimal {
        return format("")
    }


        /**
     * Formats raw digits into a FormattedDecimal with both display and parseable formats.
     *
     * @param digits String containing only digits (e.g., "123456")
     * @return FormattedDecimal containing both display format and parseable format
     */
    fun format(digits: String): FormattedDecimal {
        // Clean digits once and reuse
        val cleanedDigits = digits.sanitizeDigits()

        // Process the common formatting logic once
        val formattingResult = processDigits(cleanedDigits)

        // Create display and parseable values from the processed result
        val displayValue = createDisplayValue(formattingResult)
        val parseableValue = createParseableValue(formattingResult)

        return FormattedDecimal(displayValue, parseableValue, cleanedDigits)
    }

    /**
     * Formats a BigDecimal (most precise)
     */
    fun format(value: BigDecimal): FormattedDecimal {
        val stringValue = getRawDigits(value.toPlainString())
        return format(stringValue)
    }

    /**
     * Formats a Double (precision may be lost for very large numbers)
     */
    fun format(value: Double): FormattedDecimal {
        val stringValue = getRawDigits(value.toString())
        return format(stringValue)
    }

    /**
     * Formats a Float (precision may be lost)
     */
    fun format(value: Float): FormattedDecimal {
        val stringValue = getRawDigits(value.toString())
        return format(stringValue)
    }

    /**
     * Formats a Long (no precision loss)
     */
    fun format(value: Long): FormattedDecimal {
        val stringValue = getRawDigits(value.toString())
        return format(stringValue)
    }

    /**
     * Formats an Int (no precision loss)
     */
    fun format(value: Int): FormattedDecimal {
        val stringValue = getRawDigits(value.toString())
        return format(stringValue)
    }

    /**
     * Processes digits into a common intermediate format that can be used for both display and parseable output.
     */
    private data class FormattingResult(
        val integerPart: String,
        val decimalPart: String,
        val isEmpty: Boolean
    )

    private fun processDigits(cleanedDigits: String): FormattingResult {
        if (cleanedDigits.isEmpty()) {
            return FormattingResult(
                integerPart = "0",
                decimalPart = "0".repeat(configuration.decimalPlaces),
                isEmpty = true
            )
        }

        if (configuration.decimalPlaces == 0) {
            // Whole number
            return FormattingResult(
                integerPart = cleanedDigits,
                decimalPart = "",
                isEmpty = false
            )
        }

        // Decimal number
        return when {
            cleanedDigits.length <= configuration.decimalPlaces -> {
                // All digits become decimal part, integer part is 0
                val paddedDecimals = cleanedDigits.padStart(configuration.decimalPlaces, '0')
                FormattingResult(
                    integerPart = "0",
                    decimalPart = paddedDecimals,
                    isEmpty = false
                )
            }

            else -> {
                // Split into integer and decimal parts
                val decimals = cleanedDigits.takeLast(configuration.decimalPlaces)
                val integers = cleanedDigits.dropLast(configuration.decimalPlaces)
                val trimmedIntegers = integers.trimStart('0').ifEmpty { "0" }

                FormattingResult(
                    integerPart = trimmedIntegers,
                    decimalPart = decimals,
                    isEmpty = false
                )
            }
        }
    }

    /**
     * Creates the display value using locale-specific separators with prefix and suffix
     */
    private fun createDisplayValue(result: FormattingResult): String {
        logMessage("Creating display value for: ${result.integerPart}.${result.decimalPart}")

        val coreFormatting = if (configuration.decimalPlaces == 0) {
            // Whole number
            addThousandSeparators(result.integerPart)
        } else {
            // Decimal number
            val formattedInteger = addThousandSeparators(result.integerPart)
            val decimalSeparator = configuration.decimalSeparator.char
            "$formattedInteger$decimalSeparator${result.decimalPart}"
        }

        return "${configuration.prefix}$coreFormatting${configuration.suffix}"
    }

    /**
     * Creates the parseable value (always uses dot as decimal separator, no thousand separators, no prefix/suffix)
     */
    private fun createParseableValue(result: FormattingResult): String {
        return if (configuration.decimalPlaces == 0) {
            result.integerPart
        } else {
            "${result.integerPart}.${result.decimalPart}"
        }
    }

    /**
     * Extracts and sanitizes digits from input string
     */
    private fun getRawDigits(digits: String): String {
        return digits.sanitizeDigits()
    }

    private fun String.sanitizeDigits(): String {
        return this.filter { it.isDigit() }
            .trimStart('0')
            .take(configuration.maxDigits)
    }

    /**
     * Adds thousand separators to a number string.
     */
    private fun addThousandSeparators(number: String): String {
        val thousandSeparator = configuration.thousandSeparator
        if (thousandSeparator == ThousandSeparator.NONE || thousandSeparator.char == null || number.isEmpty()) {
            return number
        }

        // Use StringBuilder for better performance with repeated string operations
        val reversed = number.reversed()
        val result = StringBuilder(number.length + (number.length / 3)) // Pre-allocate capacity

        reversed.forEachIndexed { index, char ->
            if (index > 0 && index % 3 == 0) {
                result.append(thousandSeparator.char)
            }
            result.append(char)
        }

        return result.toString().reversed()
    }
}