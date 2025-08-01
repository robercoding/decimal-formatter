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
import dev.robercoding.decimal.formatter.compose.formatter.UiDecimalFormatter
import dev.robercoding.decimal.formatter.compose.formatter.rememberUiDecimalFormatter
import dev.robercoding.decimal.formatter.compose.model.DecimalValue
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.utils.logMessage
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        val decimalFormatterPrefix = rememberUiDecimalFormatter(DecimalFormatterConfiguration.european(), prefix = "€")
        val decimalFormatter = rememberUiDecimalFormatter(DecimalFormatterConfiguration.european())

        var outlinedAmountValuePrefix by remember { mutableStateOf(decimalFormatterPrefix.format(50524.02.toString())) }
        var outlinedAmountValueWithoutPrefix by remember { mutableStateOf(decimalFormatter.format("552423232323,64")) }

        var decimalValuePrefix by remember { mutableStateOf(decimalFormatterPrefix.format("552423232323,64")) }
        var decimalValueWithoutPrefix by remember { mutableStateOf(decimalFormatter.format("552423232323,64")) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Outlined region
            OutlinedDecimalTextFieldComposable(
                currentAmount = outlinedAmountValuePrefix,
                onValueChange = { decimalValue -> outlinedAmountValuePrefix = decimalValue },
                decimalFormatter = decimalFormatterPrefix
            )

            OutlinedDecimalTextFieldComposable(
                currentAmount = outlinedAmountValueWithoutPrefix,
                onValueChange = { decimalValue -> outlinedAmountValueWithoutPrefix = decimalValue },
                decimalFormatter = decimalFormatterPrefix
            )

            // No decor TextField region
            DecimalTextFieldComposable(
                currentAmount = decimalValuePrefix,
                onFormattedValueChange = { decimalValue -> decimalValuePrefix = decimalValue },
                decimalFormatter = decimalFormatterPrefix
            )

            DecimalTextFieldComposable(
                currentAmount = decimalValueWithoutPrefix,
                onFormattedValueChange = { decimalValue -> decimalValueWithoutPrefix = decimalValue },
                decimalFormatter = decimalFormatterPrefix
            )
        }

    }
}

@Composable
fun OutlinedDecimalTextFieldComposable(
    currentAmount: DecimalValue,
    onValueChange: (DecimalValue) -> Unit,
    decimalFormatter: UiDecimalFormatter
) {
    OutlinedDecimalTextField(
        modifier = Modifier,
        decimalValue = currentAmount,
        onValueChange = onValueChange,
        label = { Text("Price (€)", style = MaterialTheme.typography.labelSmall) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedTextColor = if (currentAmount.display == "0,00") {
                logMessage("Using gray color for zero value")
                Color.Gray
            } else {
                logMessage("Using default color for non-zero value: $currentAmount")
                MaterialTheme.colorScheme.onSurface
            },
            unfocusedTextColor = if (currentAmount.display == "0,00") {
                logMessage("Using gray color for zero value")
                Color.Gray
            } else {
                logMessage("Using default color for non-zero value: $currentAmount")
                MaterialTheme.colorScheme.onSurface
            }
        ),
        decimalFormatter = decimalFormatter
    )
}

@Composable
fun DecimalTextFieldComposable(
    currentAmount: DecimalValue,
    onFormattedValueChange: (DecimalValue) -> Unit,
    decimalFormatter: UiDecimalFormatter,
) {
    DecimalTextField(
        modifier = Modifier,
        value = currentAmount,
        onValueChange = { value ->
            onFormattedValueChange(value)
        },
        prefix = "$",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.headlineLarge,
        decimalFormatter = decimalFormatter
    )
}