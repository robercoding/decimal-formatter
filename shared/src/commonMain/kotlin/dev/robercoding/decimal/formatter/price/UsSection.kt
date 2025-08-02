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
internal fun UsSection() {

    val decimalFormatterUSD = rememberDecimalFormatter(DecimalFormatterConfiguration.usd())
    val decimalFormatterUs = rememberDecimalFormatter(DecimalFormatterConfiguration.us())

    var priceUSD by remember { mutableStateOf(decimalFormatterUSD.format("")) }
    var priceUs by remember { mutableStateOf(decimalFormatterUs.format("")) }


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Column {
            Header(
                text = "USD format",
                modifier = Modifier.fillMaxWidth()
            )

            // Euro region
            Column(modifier = Modifier.fillMaxWidth()) {
                // USD region
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
            }
        }

        // European section header
        Column {
            Header(
                text = "US format",
                modifier = Modifier.fillMaxWidth()
            )
            // European region
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
            }
        }
    }
}

