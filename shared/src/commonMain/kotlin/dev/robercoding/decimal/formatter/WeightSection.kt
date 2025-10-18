package dev.robercoding.decimal.formatter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalInputMode
import dev.robercoding.decimal.formatter.core.model.DecimalSeparator
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator

@Composable
internal fun WeightSection(
    inputMode: DecimalInputMode,
    onInputModeChange: (DecimalInputMode) -> Unit
) {
    val decimalFormatterEuropeanWeight = rememberDecimalFormatter(
        configuration = DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = 3,
            maxDigits = 6,
            suffix = " kg",
            inputMode = inputMode
        )
    )
    var weight by remember { mutableStateOf(decimalFormatterEuropeanWeight.format("")) }

    LaunchedEffect(inputMode) {
        if (weight.parseableValue.isNotEmpty() && weight.parseableValue != "0.000") {
            weight = decimalFormatterEuropeanWeight.format(weight.parseableValue)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InputModeSelector(
            selectedMode = inputMode,
            onModeChange = onInputModeChange
        )

        HeaderWithTextFields(
            header = "Weight format",
            value = weight,
            onValueChange = { weight = it },
            decimalFormatter = decimalFormatterEuropeanWeight
        )

        Text(
            text = "Debug: display=\"${weight.displayValue}\" | parseable=\"${weight.parseableValue}\" | raw=\"${weight.rawDigits}\"",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}