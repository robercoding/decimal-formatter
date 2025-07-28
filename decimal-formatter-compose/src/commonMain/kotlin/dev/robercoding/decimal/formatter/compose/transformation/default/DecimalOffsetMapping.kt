package dev.robercoding.decimal.formatter.compose.transformation.default

import androidx.compose.ui.text.input.OffsetMapping
import dev.robercoding.decimal.formatter.utils.logMessage

/**
 * Offset mapping to keep cursor position consistent during decimal formatting transformations.
 */
internal class DecimalOffsetMapping(
    private val originalText: String,
    private val transformedText: String
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        return transformedText.length
    }

    override fun transformedToOriginal(offset: Int): Int {
        return originalText.length
    }
}