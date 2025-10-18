package dev.robercoding.decimal.formatter.price

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.robercoding.decimal.formatter.Header
import dev.robercoding.decimal.formatter.HeaderWithTextFields
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalInputMode
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal

@Composable
internal fun EuSection(
    priceEuropean: FormattedDecimal?,
    priceEuro: FormattedDecimal?,
    inputMode: DecimalInputMode,
    onPriceEuroChange: (FormattedDecimal) -> Unit,
    onPriceEuropeanChange: (FormattedDecimal) -> Unit,
) {
    val decimalFormatterEuro = rememberDecimalFormatter(
        DecimalFormatterConfiguration.euro(inputMode = inputMode)
    )
    val decimalFormatterEuropean = rememberDecimalFormatter(
        DecimalFormatterConfiguration.european(inputMode = inputMode)
    )

    LaunchedEffect(inputMode) {
        priceEuro?.let {
            if (it.parseableValue.isNotEmpty() && it.parseableValue != "0.00") {
                onPriceEuroChange(decimalFormatterEuro.format(it.parseableValue))
            }
        }
        priceEuropean?.let {
            if (it.parseableValue.isNotEmpty() && it.parseableValue != "0.00") {
                onPriceEuropeanChange(decimalFormatterEuropean.format(it.parseableValue))
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

       HeaderWithTextFields(
            header = "Euro format",
            value = priceEuro,
            onValueChange = onPriceEuroChange,
            decimalFormatter = decimalFormatterEuro
        )
        priceEuro?.let {
            Text(
                text = "Debug: display=\"${it.displayValue}\" | parseable=\"${it.parseableValue}\" | raw=\"${it.rawDigits}\"",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }

        HeaderWithTextFields(
            header = "European format",
            value = priceEuropean,
            onValueChange = onPriceEuropeanChange,
            decimalFormatter = decimalFormatterEuropean
        )
        priceEuropean?.let {
            Text(
                text = "Debug: display=\"${it.displayValue}\" | parseable=\"${it.parseableValue}\" | raw=\"${it.rawDigits}\"",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}
