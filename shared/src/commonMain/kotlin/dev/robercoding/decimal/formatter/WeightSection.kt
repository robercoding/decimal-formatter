package dev.robercoding.decimal.formatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.robercoding.decimal.formatter.compose.formatter.rememberDecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import dev.robercoding.decimal.formatter.core.model.DecimalSeparator
import dev.robercoding.decimal.formatter.core.model.ThousandSeparator

@Composable
internal fun WeightSection() {
    val decimalFormatterEuropeanWeight = rememberDecimalFormatter(DecimalFormatterConfiguration(
        decimalSeparator = DecimalSeparator.COMMA,
        thousandSeparator = ThousandSeparator.DOT,
        decimalPlaces = 3,
        maxDigits = 6,
        suffix = " kg"

    ))
    var weight by remember { mutableStateOf(decimalFormatterEuropeanWeight.format("")) }

    HeaderWithTextFields(
        header = "Weight format",
        value = weight,
        onValueChange = { weight = it },
        decimalFormatter = decimalFormatterEuropeanWeight
    )
}