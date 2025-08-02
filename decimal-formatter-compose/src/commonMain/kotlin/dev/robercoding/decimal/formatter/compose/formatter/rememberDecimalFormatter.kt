package dev.robercoding.decimal.formatter.compose.formatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration

@Composable
fun rememberDecimalFormatter(
    configuration: DecimalFormatterConfiguration = DecimalFormatterConfiguration.DefaultConfiguration,
): DecimalFormatter {
    return remember(configuration) { DecimalFormatter(configuration) }
}