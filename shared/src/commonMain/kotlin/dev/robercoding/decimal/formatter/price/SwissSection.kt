package dev.robercoding.decimal.formatter.price

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.robercoding.decimal.formatter.Header
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration

@Composable
internal fun SwissSection() {

    val decimalFormatterChf = rememberDecimalFormatter(DecimalFormatterConfiguration.chf())
    val decimalFormatterSwiss = rememberDecimalFormatter(DecimalFormatterConfiguration.swiss())

    var priceChf by remember { mutableStateOf(decimalFormatterChf.format("")) }
    var priceSwiss by remember { mutableStateOf(decimalFormatterSwiss.format("")) }


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Column {
            Header(
                text = "CHF format",
                modifier = Modifier.fillMaxWidth()
            )

            // Euro region
            Column(modifier = Modifier.fillMaxWidth()) {
                // USD region
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
            }
        }

        // European section header
        Column {
            Header(
                text = "Swiss format",
                modifier = Modifier.fillMaxWidth()
            )
            // European region
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
            }
        }
    }
}

