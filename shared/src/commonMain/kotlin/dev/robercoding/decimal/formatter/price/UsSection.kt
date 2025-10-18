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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.robercoding.decimal.formatter.Header
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalInputMode

@Composable
internal fun UsSection(inputMode: DecimalInputMode) {

    val decimalFormatterUSD = rememberDecimalFormatter(
        DecimalFormatterConfiguration.usd(inputMode = inputMode)
    )
    val decimalFormatterUs = rememberDecimalFormatter(
        DecimalFormatterConfiguration.us(inputMode = inputMode)
    )

    var priceUSD by remember { mutableStateOf(decimalFormatterUSD.format("")) }
    var priceUs by remember { mutableStateOf(decimalFormatterUs.format("")) }

    LaunchedEffect(inputMode) {
        if (priceUSD.parseableValue.isNotEmpty() && priceUSD.parseableValue != "0.00") {
            priceUSD = decimalFormatterUSD.format(priceUSD.parseableValue)
        }
        if (priceUs.parseableValue.isNotEmpty() && priceUs.parseableValue != "0.00") {
            priceUs = decimalFormatterUs.format(priceUs.parseableValue)
        }
    }


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Column {
            Header(
                text = "USD format",
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedDecimalTextFieldComposable(
                    currentAmount = priceUSD,
                    onValueChange = { priceUSD = it },
                    decimalFormatter = decimalFormatterUSD
                )
                Spacer(modifier = Modifier.height(12.dp))
                DecimalTextFieldComposable(
                    currentAmount = priceUSD,
                    onValueChange = { priceUSD = it },
                    decimalFormatter = decimalFormatterUSD
                )
                Text(
                    text = "Debug: display=\"${priceUSD.displayValue}\" | parseable=\"${priceUSD.parseableValue}\" | raw=\"${priceUSD.rawDigits}\"",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        Column {
            Header(
                text = "US format",
                modifier = Modifier.fillMaxWidth()
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedDecimalTextFieldComposable(
                    currentAmount = priceUs,
                    onValueChange = { priceUs = it },
                    decimalFormatter = decimalFormatterUs
                )
                Spacer(modifier = Modifier.height(12.dp))
                DecimalTextFieldComposable(
                    currentAmount = priceUs,
                    onValueChange = { priceUs = it },
                    decimalFormatter = decimalFormatterUs
                )
                Text(
                    text = "Debug: display=\"${priceUs.displayValue}\" | parseable=\"${priceUs.parseableValue}\" | raw=\"${priceUs.rawDigits}\"",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

