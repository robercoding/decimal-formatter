package dev.robercoding.decimal.formatter.percentage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import dev.robercoding.decimal.formatter.HeaderWithTextFields
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration

@Composable
internal fun PercentageSection() {
    val decimalFormatterPercentage = rememberDecimalFormatter(DecimalFormatterConfiguration.percentage())
    var percentage by remember { mutableStateOf(decimalFormatterPercentage.format("")) }

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
}