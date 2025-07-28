package dev.robercoding.decimal.formatter.compose.utils

import dev.robercoding.decimal.formatter.compose.model.DecimalValueProcessed
import dev.robercoding.decimal.formatter.core.DecimalFormatter
import dev.robercoding.decimal.formatter.utils.logMessage

/**
 * Processes a new input value and returns all formatted variations.
 * This extracts the common logic used in both OutlinedDecimalTextField and BasicDecimalTextField.
 *
 * @param newValue The raw input value from the text field
 * @param prefix Optional prefix to add to the formatted value
 * @param decimalFormatter The formatter to use for decimal formatting
 * @return [DecimalValueProcessed] containing all variations of the processed value
 */
fun processDecimalValueChange(
    newValue: String,
    prefix: String? = null,
    decimalFormatter: DecimalFormatter
): DecimalValueProcessed {
    logMessage("New value entered: $newValue")
    val raw = decimalFormatter.getRawDigits(newValue)

    val formatted = decimalFormatter.format(raw)

    val prefix = prefix?.takeIf { it.isNotBlank() } ?: ""
    val formattedDecimalWithPrefix = "$prefix$formatted"
    logMessage(
        "Raw value: $raw \n" +
            "Formatted value: $formattedDecimalWithPrefix (with prefix: $prefix) \n" +
            "Formatted value without prefix: $formatted"
    )
    return DecimalValueProcessed(
        raw = raw,
        formatted = formatted,
        formattedWithPrefix = formattedDecimalWithPrefix
    )
}



