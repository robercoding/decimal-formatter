package dev.robercoding.decimal.formatter.compose.formatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.robercoding.decimal.formatter.core.DecimalFormatter
import dev.robercoding.decimal.formatter.core.DecimalFormatterConfiguration

@Composable
fun rememberUiDecimalFormatter(configuration: DecimalFormatterConfiguration, prefix: String? = null): UiDecimalFormatter {
    val decimalFormatter = remember(configuration) { DecimalFormatter(configuration) }
    return remember(configuration) {
        UiDecimalFormatter(decimalFormatter, prefix)
    }
}