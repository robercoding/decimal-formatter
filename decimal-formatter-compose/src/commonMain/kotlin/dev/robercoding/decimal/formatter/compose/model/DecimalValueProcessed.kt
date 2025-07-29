package dev.robercoding.decimal.formatter.compose.model

/**
 * Data class holding the processed decimal values in different formats.
 *
 * @property raw The raw digits only (e.g., "123456")
 * @property formatted The formatted decimal without prefix (e.g., "1.234,56" or "1'234.56" or "1,234.56")
 * @property formattedWithPrefix The formatted decimal with optional prefix (e.g., "$1.234,56" or "CHF 1'234.56" or "USD 1,234.56")
 */
data class DecimalValueProcessed(
    val raw: String,
    val formatted: String,
    val formattedWithPrefix: String?
)