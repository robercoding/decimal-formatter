package dev.robercoding.decimal.formatter.compose.formatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration

@Composable
fun rememberUiDecimalFormatter(configuration: DecimalFormatterConfiguration = DecimalFormatterConfiguration.DefaultConfiguration, prefix: String? = null): UiDecimalFormatter {
    val decimalFormatter = remember(configuration) { DecimalFormatter(configuration) }
    return remember(configuration) {
        UiDecimalFormatter(decimalFormatter, prefix)
    }
}