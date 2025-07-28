package dev.robercoding.decimal.formatter.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.robercoding.decimal.formatter.core.DecimalFormatter
import dev.robercoding.decimal.formatter.core.DecimalFormatterConfiguration

@Composable
internal fun rememberDecimalFormatter(configuration: DecimalFormatterConfiguration): DecimalFormatter {
    return remember(configuration) {
        DecimalFormatter(configuration)
    }
}