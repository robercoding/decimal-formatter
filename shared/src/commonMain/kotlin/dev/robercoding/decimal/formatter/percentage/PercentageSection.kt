package dev.robercoding.decimal.formatter.percentage

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
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import dev.robercoding.decimal.formatter.HeaderWithTextFields
import dev.robercoding.decimal.formatter.InputModeSelector
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalInputMode

@Composable
internal fun PercentageSection(
    inputMode: DecimalInputMode,
    onInputModeChange: (DecimalInputMode) -> Unit
) {
    val decimalFormatterPercentage = rememberDecimalFormatter(
        DecimalFormatterConfiguration.percentage(inputMode = inputMode)
    )
    var percentage by remember { mutableStateOf(decimalFormatterPercentage.format("")) }

    LaunchedEffect(inputMode) {
        if (percentage.parseableValue.isNotEmpty() && percentage.parseableValue != "0.00") {
            percentage = decimalFormatterPercentage.format(percentage.parseableValue)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InputModeSelector(
            selectedMode = inputMode,
            onModeChange = onInputModeChange
        )

        HeaderWithTextFields(
            header = "Percentage format",
            value = percentage,
            onValueChange = {
                percentage = if (it.parseableValue.toBigDecimal() > 100.0) {
                    decimalFormatterPercentage.format("100.00")
                } else {
                    it
                }
            },
            decimalFormatter = decimalFormatterPercentage
        )

        Text(
            text = "Debug: display=\"${percentage.displayValue}\" | parseable=\"${percentage.parseableValue}\" | raw=\"${percentage.rawDigits}\"",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}