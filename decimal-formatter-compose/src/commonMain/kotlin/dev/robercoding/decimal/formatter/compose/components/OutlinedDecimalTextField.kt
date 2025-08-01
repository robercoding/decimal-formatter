package dev.robercoding.decimal.formatter.compose.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import dev.robercoding.decimal.formatter.compose.formatter.UiDecimalFormatter
import dev.robercoding.decimal.formatter.compose.formatter.rememberUiDecimalFormatter
import dev.robercoding.decimal.formatter.compose.model.DecimalValue
import dev.robercoding.decimal.formatter.compose.transformation.default.DecimalVisualTransformation
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration

/**
 * A text field component for decimal number input with automatic formatting.
 *
 * This component automatically formats numbers with thousand separators and decimal places
 * as the user types. It handles various international number formats and maintains proper
 * cursor positioning.
 *
 * @param value The current formatted value (e.g., "1.234,56")
 * @param onValueChange Called when the value changes with the new formatted value
 * @param modifier Modifier to be applied to the text field
 * @param configuration Configuration for decimal and thousand separators, decimal places, etc.
 * @param enabled Controls the enabled state of the text field
 * @param readOnly Controls whether the text field is read-only
 * @param textStyle The style to be applied to the input text
 * @param label Optional label to be displayed inside the text field
 * @param placeholder Optional placeholder to be displayed when the text field is empty
 * @param leadingIcon Optional icon to be displayed at the beginning of the text field
 * @param trailingIcon Optional icon to be displayed at the end of the text field
 * @param supportingText Optional supporting text to be displayed below the text field
 * @param isError Indicates if the text field's current value is in error state
 * @param keyboardOptions Software keyboard options that contains configuration
 * @param keyboardActions When the input service emits an IME action
 * @param singleLine When true, this text field becomes a single horizontally scrolling text field
 * @param maxLines The maximum height in terms of maximum number of visible lines
 * @param interactionSource The [MutableInteractionSource] representing the stream of interactions
 * @param shape The shape of the text field's container
 * @param colors Colors to be used for the text field
 */
@Composable
fun OutlinedDecimalTextField(
    decimalValue: DecimalValue, // User manages the full state
    onValueChange: (DecimalValue) -> Unit,
    modifier: Modifier = Modifier,
    decimalFormatter: UiDecimalFormatter = rememberUiDecimalFormatter(DecimalFormatterConfiguration.DefaultConfiguration),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {

    OutlinedTextField(
        value = decimalValue.rawDigits,
        onValueChange = {
            val decimalValue = decimalFormatter.format(it)
            onValueChange(decimalValue)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = DecimalVisualTransformation(decimalValue = decimalValue),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}
