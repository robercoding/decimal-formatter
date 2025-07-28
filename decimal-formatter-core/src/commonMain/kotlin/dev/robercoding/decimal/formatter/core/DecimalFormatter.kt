package dev.robercoding.decimal.formatter.core

import dev.robercoding.decimal.formatter.model.ThousandSeparator
import dev.robercoding.decimal.formatter.utils.logMessage

/**
 * A formatter for decimal numbers with configurable separators and decimal places.
 */
public class DecimalFormatter(private val configuration: DecimalFormatterConfiguration) {

    /**
     * Formats raw digits into a decimal number with thousand separators.
     *
     * @param digits String containing only digits (e.g., "123456")
     * @return Formatted string (e.g., "1.234,56" for European format)
     */
    fun format(digits: String): String {
        return if (configuration.decimalPlaces == 0) {
            formatWholeNumber(digits)
        } else {
            formatDecimalNumber(digits)
        }
    }

    /**
     * Formats a whole number (no decimal places).
     */
    private fun formatWholeNumber(digits: String): String {
        logMessage("Formatting whole number: $digits")
        if (digits.isEmpty()) return "0"

        val cleanedDigits = digits.prepareForFormatting()

        if (cleanedDigits.isEmpty()) return "0"

        return addThousandSeparators(cleanedDigits)
    }

    /**
     * Formats a decimal number with the configured decimal places.
     */
    private fun formatDecimalNumber(digits: String): String {
        logMessage("Formatting decimal number: $digits")

        val cleanedDigits = digits.prepareForFormatting()
        val decimalSeparator = configuration.decimalSeparator
        val decimalPlaces = configuration.decimalPlaces

        return when {
            cleanedDigits.isEmpty() -> {
                "0${decimalSeparator.char}${"0".repeat(decimalPlaces)}"
            }

            cleanedDigits.length <= decimalPlaces -> {
                // Whenever the length is less than or equal to decimal places, m
                // e.g: "1" becomes "0,01" for 2 decimal places
                // e.g: "123" becomes "0,123" for 3 decimal places
                val paddedDecimals = cleanedDigits.padStart(decimalPlaces, '0')
                "0${decimalSeparator.char}$paddedDecimals"
            }

            else -> {
                val decimals = cleanedDigits.takeLast(decimalPlaces)
                val integers = cleanedDigits.dropLast(decimalPlaces)

                // Remove leading zeros from integer part, but keep at least one digit if empty
                val trimmedIntegers = integers.trimStart('0').ifEmpty { "0" }
                val integersWithSeparators = addThousandSeparators(trimmedIntegers)

                "$integersWithSeparators${decimalSeparator.char}$decimals"
            }
        }
    }

    fun getRawDigits(digits: String): String {
        return digits.sanitizeDigits()
    }

    private fun String.sanitizeDigits(): String {
        return this.filter { it.isDigit() }.prepareForFormatting()
    }

    private fun String.prepareForFormatting(): String {
        return this.trimStart('0')
            .take(configuration.maxDigits)
    }

    /**
     * Adds thousand separators to a number string.
     *
     * @param number The number string to format (e.g., "12345678")
     *
     * @return The formatted string with thousand separators (e.g., "12.345.678" for European format)
     *
     * This method reverses the string to easily insert separators every 3 digits,
     * then reverses it back to the original order.
     *
     * The steps are the following:
     * 1. Reverse the string to make it easier to add separators every 3 digits, e.g: "12345678" -> "87654321"
     * 2. Iterate through the reversed string and append the separator every 3 digits. e.g: "87654321" with dot separator -> "87.654.321"
     * 3. Reverse the string back to the original order, e.g: "87.654.321" -> "123.456.78"
     */
    private fun addThousandSeparators(number: String): String {
        val thousandSeparator = configuration.thousandSeparator
        if (thousandSeparator == ThousandSeparator.NONE || thousandSeparator.char == null) {
            return number
        }

        if (number.isEmpty()) return number

        // Reverse the string to make it easier to add separators every 3 digits
        // e.g: "12345678" -> "87654321"
        // e.g: "12345678" with dot separator -> "87.654.321"
        val reversed = number.reversed()
        val result = StringBuilder()

        reversed.forEachIndexed { index, char ->
            val isSeparatorPosition = index > 0 && index % 3 == 0
            if (isSeparatorPosition) {
                result.append(thousandSeparator.char)
            }
            result.append(char)
        }

        // After adding separators, we will reverse it back
        // e.g: "87.654.321" -> "123.456.78"
        return result.toString().reversed()
    }
}
