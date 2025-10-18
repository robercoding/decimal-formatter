package dev.robercoding.decimal.formatter.core.formatter

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import dev.robercoding.decimal.formatter.core.model.DecimalInputMode
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator
import dev.robercoding.decimal.formatter.core.utils.logMessage

/**
 * A formatter for decimal numbers with configurable separators and decimal places.
 *
 * This class is immutable and thread-safe. Instances can be safely shared across
 * multiple threads and reused for multiple formatting operations.
 */
class DecimalFormatter(private val configuration: DecimalFormatterConfiguration) {

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

        // In FIXED_DECIMALS mode, rawDigits should preserve the decimal representation with trailing zeros
        // This ensures that when continuing to type after mode switches, the full precision is maintained
        val rawDigits = when (configuration.inputMode) {
            DecimalInputMode.FRACTIONAL -> cleanedDigits
            DecimalInputMode.FIXED_DECIMALS -> parseableValue
        }

        return FormattedDecimal(displayValue, parseableValue, rawDigits)
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
        return when (configuration.inputMode) {
            DecimalInputMode.FRACTIONAL -> processFractionalMode(cleanedDigits)
            DecimalInputMode.FIXED_DECIMALS -> processFixedDecimalsMode(cleanedDigits)
        }
    }

    /**
     * Process digits in FRACTIONAL mode (original behavior).
     * All digits fill decimal places from the right.
     */
    private fun processFractionalMode(cleanedDigits: String): FormattingResult {
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
     * Process digits in FIXED_DECIMALS mode.
     * Supports parsing formatted input with decimal separators.
     * Pads decimal places with zeros.
     */
    private fun processFixedDecimalsMode(input: String): FormattingResult {
        if (input.isEmpty()) {
            return FormattingResult(
                integerPart = "0",
                decimalPart = "0".repeat(configuration.decimalPlaces),
                isEmpty = true
            )
        }

        // Find decimal separator in the input (. or ,)
        val decimalIndex = input.indexOfFirst { it == '.' || it == ',' }

        if (decimalIndex == -1) {
            // No decimal separator found - treat as whole number
            val cleanedInteger = input.filter { it.isDigit() }.trimStart('0').ifEmpty { "0" }

            if (configuration.decimalPlaces == 0) {
                return FormattingResult(
                    integerPart = cleanedInteger,
                    decimalPart = "",
                    isEmpty = false
                )
            }

            return FormattingResult(
                integerPart = cleanedInteger,
                decimalPart = "0".repeat(configuration.decimalPlaces),
                isEmpty = false
            )
        }

        // Decimal separator found - parse both parts
        val integerPart = input.substring(0, decimalIndex).filter { it.isDigit() }
        val decimalPart = input.substring(decimalIndex + 1).filter { it.isDigit() }

        val cleanedInteger = integerPart.trimStart('0').ifEmpty { "0" }

        if (configuration.decimalPlaces == 0) {
            // Ignore decimal part if decimalPlaces is 0
            return FormattingResult(
                integerPart = cleanedInteger,
                decimalPart = "",
                isEmpty = false
            )
        }

        // Pad or truncate to configured decimal places
        val formattedDecimal = when {
            decimalPart.length < configuration.decimalPlaces -> {
                // Pad with trailing zeros
                decimalPart.padEnd(configuration.decimalPlaces, '0')
            }
            decimalPart.length > configuration.decimalPlaces -> {
                // Truncate to configured decimal places
                decimalPart.take(configuration.decimalPlaces)
            }
            else -> {
                decimalPart
            }
        }

        return FormattingResult(
            integerPart = cleanedInteger,
            decimalPart = formattedDecimal,
            isEmpty = false
        )
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
        return when (configuration.inputMode) {
            DecimalInputMode.FRACTIONAL -> {
                // Original behavior: only keep digits, trim leading zeros
                this.filter { it.isDigit() }
                    .trimStart('0')
                    .take(configuration.maxDigits)
            }
            DecimalInputMode.FIXED_DECIMALS -> {
                // Keep digits and decimal separators (. and ,)
                // Don't trim leading zeros before decimal - let processFixedDecimalsMode handle it
                this.filter { it.isDigit() || it == '.' || it == ',' }
                    .take(configuration.maxDigits + 1) // +1 to account for decimal separator
            }
        }
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DecimalFormatter) return false
        return configuration == other.configuration
    }

    override fun hashCode(): Int {
        return configuration.hashCode()
    }

    override fun toString(): String {
        return "DecimalFormatter(configuration=$configuration)"
    }
}