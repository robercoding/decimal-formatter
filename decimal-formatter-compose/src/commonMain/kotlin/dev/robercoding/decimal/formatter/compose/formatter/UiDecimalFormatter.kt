package dev.robercoding.decimal.formatter.compose.formatter

import dev.robercoding.decimal.formatter.compose.model.DecimalValue
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter

data class UiDecimalFormatter(
    private val decimalFormatter: DecimalFormatter = DecimalFormatter.DefaultFormatter,
    private val prefix: String? = null
) {
    companion object {
        public val DefaultUiFormatter: UiDecimalFormatter
            get() = UiDecimalFormatter(DecimalFormatter.DefaultFormatter, null)
    }
    fun format(value: String): DecimalValue {
        val rawDigits = decimalFormatter.getRawDigits(value)
        val display = decimalFormatter.format(rawDigits)
        val fullDisplay = prefix?.takeIf { it.isNotBlank() }?.let { "$it$display" }
        return DecimalValue(rawDigits, display, fullDisplay)
    }
    
    fun format(value: Double): DecimalValue = format(value.toString())

    fun format(value: Float): DecimalValue = format(value.toString())

    fun format(value: Long): DecimalValue = format(value.toString())

    fun format(value: Int): DecimalValue = format(value.toString())
}