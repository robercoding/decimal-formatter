package dev.robercoding.decimal.formatter

import dev.robercoding.decimal.formatter.core.model.DecimalInputMode
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal

data class UiState(
    val priceEuropean: FormattedDecimal? = null,
    val priceEuro: FormattedDecimal? = null,
    val percentageInputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL,
    val weightInputMode: DecimalInputMode = DecimalInputMode.FIXED_DECIMALS,
    val priceInputMode: DecimalInputMode = DecimalInputMode.FRACTIONAL
)