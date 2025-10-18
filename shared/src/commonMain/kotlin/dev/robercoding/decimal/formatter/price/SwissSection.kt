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
internal fun SwissSection(inputMode: DecimalInputMode) {

    val decimalFormatterChf = rememberDecimalFormatter(
        DecimalFormatterConfiguration.chf(inputMode = inputMode)
    )
    val decimalFormatterSwiss = rememberDecimalFormatter(
        DecimalFormatterConfiguration.swiss(inputMode = inputMode)
    )

    var priceChf by remember { mutableStateOf(decimalFormatterChf.format("")) }
    var priceSwiss by remember { mutableStateOf(decimalFormatterSwiss.format("")) }

    LaunchedEffect(inputMode) {
        if (priceChf.parseableValue.isNotEmpty() && priceChf.parseableValue != "0.00") {
            priceChf = decimalFormatterChf.format(priceChf.parseableValue)
        }
        if (priceSwiss.parseableValue.isNotEmpty() && priceSwiss.parseableValue != "0.00") {
            priceSwiss = decimalFormatterSwiss.format(priceSwiss.parseableValue)
        }
    }


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Column {
            Header(
                text = "CHF format",
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedDecimalTextFieldComposable(
                    currentAmount = priceChf,
                    onValueChange = { priceChf = it },
                    decimalFormatter = decimalFormatterChf
                )
                Spacer(modifier = Modifier.height(12.dp))
                DecimalTextFieldComposable(
                    currentAmount = priceChf,
                    onValueChange = { priceChf = it },
                    decimalFormatter = decimalFormatterChf
                )
                Text(
                    text = "Debug: display=\"${priceChf.displayValue}\" | parseable=\"${priceChf.parseableValue}\" | raw=\"${priceChf.rawDigits}\"",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        Column {
            Header(
                text = "Swiss format",
                modifier = Modifier.fillMaxWidth()
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedDecimalTextFieldComposable(
                    currentAmount = priceSwiss,
                    onValueChange = { priceSwiss = it },
                    decimalFormatter = decimalFormatterSwiss
                )
                Spacer(modifier = Modifier.height(12.dp))
                DecimalTextFieldComposable(
                    currentAmount = priceSwiss,
                    onValueChange = { priceSwiss = it },
                    decimalFormatter = decimalFormatterSwiss
                )
                Text(
                    text = "Debug: display=\"${priceSwiss.displayValue}\" | parseable=\"${priceSwiss.parseableValue}\" | raw=\"${priceSwiss.rawDigits}\"",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

