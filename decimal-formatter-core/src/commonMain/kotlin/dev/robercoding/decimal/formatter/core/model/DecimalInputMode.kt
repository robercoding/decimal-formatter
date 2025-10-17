package dev.robercoding.decimal.formatter.core.model

/**
 * Enum representing how input digits should be interpreted when formatting.
 */
enum class DecimalInputMode {
    /**
     * Treats all digits as filling decimal places from the right (fractional mode).
     *
     * This is the traditional "cents mode" where digits represent the smallest unit.
     *
     * Examples with 2 decimal places:
     * - "1" → "0.01" (1 cent)
     * - "123" → "1.23" ($1.23)
     * - "123456" → "1,234.56"
     */
    FRACTIONAL,

    /**
     * Treats input as a complete number with fixed decimal places.
     *
     * Raw digits are treated as whole numbers and decimal places are padded with zeros.
     * Also supports parsing formatted input containing decimal separators (. or ,).
     *
     * Examples with 3 decimal places:
     * - "1" → "1.000"
     * - "14" → "14.000"
     * - "1.45" → "1.450" (parsed with separator)
     * - "14.50" → "14.500" (parsed with separator)
     * - "14.500" → "14.500" (parsed with separator)
     *
     * This mode is ideal for:
     * - Weight/measurement inputs
     * - Scientific notation
     * - Any scenario where trailing zeros should be visible
     */
    FIXED_DECIMALS
}
