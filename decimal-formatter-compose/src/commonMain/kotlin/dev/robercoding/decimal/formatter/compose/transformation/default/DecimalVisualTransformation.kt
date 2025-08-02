package dev.robercoding.decimal.formatter.compose.transformation.default

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal

/**
 * Visual transformation for displaying decimal values in a formatted manner.
 */
internal class DecimalVisualTransformation(
    private val formattedDecimal: FormattedDecimal,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val displayText = formattedDecimal.displayValue
        return TransformedText(
            AnnotatedString(displayText),
            DecimalOffsetMapping(
                originalText = text.text,
                transformedText = displayText
            )
        )
    }
}