package dev.robercoding.decimal.formatter.price

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.robercoding.decimal.formatter.Header
import dev.robercoding.decimal.formatter.HeaderWithTextFields
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal

@Composable
internal fun EuSection(
    priceEuropean: FormattedDecimal?, // VM example
    priceEuro: FormattedDecimal?, // VM example
    onPriceEuroChange: (FormattedDecimal) -> Unit,
    onPriceEuropeanChange: (FormattedDecimal) -> Unit,
) {
    val decimalFormatterEuro = rememberDecimalFormatter(DecimalFormatterConfiguration.euro())
    val decimalFormatterEuropean = rememberDecimalFormatter(DecimalFormatterConfiguration.european())

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

       HeaderWithTextFields(
            header = "Euro format",
            value = priceEuro,
            onValueChange = onPriceEuroChange,
            decimalFormatter = decimalFormatterEuro
        )

        // European section header
        HeaderWithTextFields(
            header = "European format",
            value = priceEuropean,
            onValueChange = onPriceEuropeanChange,
            decimalFormatter = decimalFormatterEuropean
        )
    }
}
