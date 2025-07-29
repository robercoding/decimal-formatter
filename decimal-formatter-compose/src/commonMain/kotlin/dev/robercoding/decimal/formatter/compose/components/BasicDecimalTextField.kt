package dev.robercoding.decimal.formatter.compose.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import dev.robercoding.decimal.formatter.compose.model.DecimalValueProcessed
import dev.robercoding.decimal.formatter.compose.transformation.default.DecimalVisualTransformation
import dev.robercoding.decimal.formatter.core.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.DefaultConfiguration

/**
 * A basic text field component for decimal number input with automatic formatting.
 *
 * This component provides the same decimal formatting functionality as OutlinedDecimalTextField
 * but without any styling - no borders, no outline, no background. Just plain text editing
 * with decimal formatting and prefix support.
 *
 * @param value The current formatted value (e.g., "1.234,56")
 * @param onValueChange Called when the value changes with the new formatted value
 * @param modifier Modifier to be applied to the text field
 * @param configuration Configuration for decimal and thousand separators, decimal places, etc.
 * @param prefix Optional prefix to show before the number (e.g., "€ ")
 * @param enabled Controls the enabled state of the text field
 * @param readOnly Controls whether the text field is read-only
 * @param textStyle The style to be applied to the input text
 * @param keyboardOptions Software keyboard options that contains configuration
 * @param keyboardActions When the input service emits an IME action
 * @param singleLine When true, this text field becomes a single horizontally scrolling text field
 * @param maxLines The maximum height in terms of maximum number of visible lines
 * @param minLines The minimum height in terms of minimum number of visible lines
 * @param interactionSource The [MutableInteractionSource] representing the stream of interactions
 * @param cursorBrush The brush to paint the cursor with
 * @param decorationBox Allows to add decorations around text field, such as icon, placeholder, helper messages or similar
 */
@Composable
fun BasicDecimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onTransform: (DecimalValueProcessed) -> Unit = {},
    modifier: Modifier = Modifier,
    configuration: DecimalFormatterConfiguration = remember { DecimalFormatterConfiguration.Companion.DefaultConfiguration },
    prefix: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit = { innerTextField -> innerTextField() }
) {
    val decimalFormatter = rememberDecimalFormatter(configuration)
    var internalValue by remember(value) { mutableStateOf(decimalFormatter.getRawDigits(value)) }

    val transformation by remember(prefix, configuration) {
        mutableStateOf(DecimalVisualTransformation(decimalFormatter, prefix, onTransform))
    }

    BasicTextField(
        value = internalValue,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = transformation,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = decorationBox
    )
}
