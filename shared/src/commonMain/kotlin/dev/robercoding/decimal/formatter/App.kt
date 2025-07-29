package dev.robercoding.decimal.formatter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.robercoding.decimal.formatter.compose.components.DecimalTextField
import dev.robercoding.decimal.formatter.compose.components.OutlinedDecimalTextField
import dev.robercoding.decimal.formatter.core.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.utils.logMessage
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize().systemBarsPadding()) {

        var currentAmountPrefix by remember { mutableStateOf("552423232323,64") }
        var currentAmountWithoutPrefix by remember { mutableStateOf("552423232323,64") }
        var currentAmountBasic by remember { mutableStateOf("552423232323,64") }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DecimalTextFieldComposable(
                currentAmount = currentAmountPrefix,
                prefix = "€ ",
                onFormattedValueChange = { formattedValue ->
                    currentAmountPrefix = formattedValue
                }
            )

            DecimalTextFieldComposable(
                currentAmount = currentAmountWithoutPrefix,
                prefix = null,
                onFormattedValueChange = { formattedValue ->
                    currentAmountWithoutPrefix = formattedValue
                }
            )

            BasicTextFieldComposable(
                currentAmount = currentAmountBasic,
                onFormattedValueChange = { formattedValue ->
                    currentAmountBasic = formattedValue
                }
            )
        }

    }
}

@Composable
fun DecimalTextFieldComposable(
    currentAmount: String,
    onFormattedValueChange: (String) -> Unit,
    prefix: String?,
) {
    val currentConfiguration = remember { DecimalFormatterConfiguration.european() }

    OutlinedDecimalTextField(
        modifier = Modifier,
        value = currentAmount,
        onValueChange = { value ->
            onFormattedValueChange(value)
        },
        onDecimalValueChange = { formattedDecimalValue ->
            logMessage("Display value: ${formattedDecimalValue.display}")
            logMessage("Full Display value: ${formattedDecimalValue.display}")
            logMessage("Raw Digits: ${formattedDecimalValue.rawDigits}")
        },
        prefix = prefix,
        label = { Text("Price (€)", style = MaterialTheme.typography.labelSmall) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedTextColor = if (currentAmount == "0,00") {
                logMessage("Using gray color for zero value")
                Color.Gray
            } else {
                logMessage("Using default color for non-zero value: $currentAmount")
                MaterialTheme.colorScheme.onSurface
            },
            unfocusedTextColor = if (currentAmount == "0,00") {
                logMessage("Using gray color for zero value")
                Color.Gray
            } else {
                logMessage("Using default color for non-zero value: $currentAmount")
                MaterialTheme.colorScheme.onSurface
            }
        ),
        configuration = currentConfiguration

    )
}

@Composable
fun BasicTextFieldComposable(
    currentAmount: String,
    onFormattedValueChange: (String) -> Unit,
) {
    val currentConfiguration = remember { DecimalFormatterConfiguration.us() }

    DecimalTextField(
        modifier = Modifier,
        value = currentAmount,
        onValueChange = { value ->
            onFormattedValueChange(value)
        },
        onDecimalValueChange = { formattedDecimalValue ->
            logMessage("Display value: ${formattedDecimalValue.display}")
            logMessage("Full Display value: ${formattedDecimalValue.display}")
            logMessage("Raw Digits: ${formattedDecimalValue.rawDigits}")
        },
        prefix = "$",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.headlineLarge,
        configuration = currentConfiguration
    )
}