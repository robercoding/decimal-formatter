package dev.robercoding.decimal.formatter.compose.model

/**
 * Data class holding the processed decimal values in different formats.
 */
data class DecimalValueProcessed(
    /** Raw digits only (e.g., "123456") */
    val raw: String,
    /** Formatted decimal without prefix (e.g., "1.234,56") */
    val formatted: String,
    /** Formatted decimal with prefix (e.g., "€ 1.234,56") */
    val formattedWithPrefix: String
)