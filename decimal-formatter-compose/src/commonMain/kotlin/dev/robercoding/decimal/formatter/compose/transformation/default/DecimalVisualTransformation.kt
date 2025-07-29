package dev.robercoding.decimal.formatter.compose.transformation.default

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.robercoding.decimal.formatter.compose.model.DecimalValueProcessed
import dev.robercoding.decimal.formatter.core.DecimalFormatter
import dev.robercoding.decimal.formatter.utils.logMessage

/**
 * Visual transformation for decimal number formatting.
 *
 * This transformation formats the input text as a decimal number, optionally adding a prefix in your composable UI.
 */
internal class DecimalVisualTransformation(
    private val decimalFormatter: DecimalFormatter,
    private val prefix: String? = null,
    private val onTransformNewText: (DecimalValueProcessed) -> Unit
) : VisualTransformation {

    private var previousOriginalText: String? = null

    override fun filter(text: AnnotatedString): TransformedText {
        if (prefix != null) {
            require(prefix.isNotBlank()) { "Prefix must not be blank" }
        }

        val originalText = text.text
        val formatted = decimalFormatter.format(originalText)
        val formattedWithPrefix: String? = formatted.takeIf { prefix != null }?.let { "$prefix$it" }

        if (previousOriginalText != originalText) {
            previousOriginalText = originalText
            val decimalValue = getDecimalValueProcessed(
                originalText = originalText,
                formatted = formatted,
                formattedWithPrefix = formattedWithPrefix
            )
            onTransformNewText(decimalValue)
        }

        val transformedText = formattedWithPrefix ?: formatted
        return TransformedText(
            AnnotatedString(text = transformedText),
            DecimalOffsetMapping(originalText, transformedText)
        )
    }

    private fun getDecimalValueProcessed(
        originalText: String,
        formatted: String,
        formattedWithPrefix: String? = null
    ): DecimalValueProcessed {
        val raw = decimalFormatter.getRawDigits(originalText)
        logMessage(
            "Raw value: $raw \n" +
                "Formatted value: $formattedWithPrefix (with prefix: $prefix) \n" +
                "Formatted value without prefix: $formatted"
        )
        val decimalValue = DecimalValueProcessed(
            raw = raw,
            formatted = formatted,
            formattedWithPrefix = formattedWithPrefix
        )
        return decimalValue
    }
}