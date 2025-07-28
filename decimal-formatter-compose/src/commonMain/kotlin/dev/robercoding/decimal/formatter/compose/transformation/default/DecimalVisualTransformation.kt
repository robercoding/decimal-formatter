package dev.robercoding.decimal.formatter.compose.transformation.default

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.robercoding.decimal.formatter.core.DecimalFormatter

/**
 * Visual transformation for decimal number formatting.
 *
 * This transformation formats the input text as a decimal number, optionally adding a prefix in your composable UI.
 */
internal class DecimalVisualTransformation(
    private val decimalFormatter: DecimalFormatter,
    private val prefix: String? = null
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        if (prefix != null) { require(prefix.isNotBlank()) { "Prefix must not be blank" } }

        val originalText = text.text
        var formatted = decimalFormatter.format(originalText)

        formatted = if(prefix != null) {
            "$prefix$formatted"
        } else {
            formatted
        }

        return TransformedText(
            AnnotatedString(formatted),
            DecimalOffsetMapping(originalText, formatted)
        )
    }
}