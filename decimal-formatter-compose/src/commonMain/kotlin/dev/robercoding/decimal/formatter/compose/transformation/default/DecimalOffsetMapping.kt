package dev.robercoding.decimal.formatter.compose.transformation.default

import androidx.compose.ui.text.input.OffsetMapping

/**
 * Offset mapping that positions the cursor at the end of the text during decimal formatting transformations.
 *
 * This implementation uses a simplified cursor strategy where the cursor is always placed at the end
 * of the text, regardless of where the user initially placed it. This design choice ensures:
 * 
 * This behavior is intentional for use cases where users type continuously
 * (e.g., price input, calculator interfaces) rather than editing mid-string.
 *
 * @param originalText The raw text without formatting (digits only)
 * @param transformedText The formatted text with separators, prefix, and suffix
 */
internal class DecimalOffsetMapping(
    private val originalText: String,
    private val transformedText: String
) : OffsetMapping {

    /**
     * Maps a position in the original (unformatted) text to the transformed (formatted) text.
     * Always returns the end position to ensure cursor stays at the end during typing.
     */
    override fun originalToTransformed(offset: Int): Int {
        return transformedText.length
    }

    /**
     * Maps a position in the transformed (formatted) text back to the original (unformatted) text.
     * Always returns the end position to ensure cursor stays at the end during typing.
     */
    override fun transformedToOriginal(offset: Int): Int {
        return originalText.length
    }
}