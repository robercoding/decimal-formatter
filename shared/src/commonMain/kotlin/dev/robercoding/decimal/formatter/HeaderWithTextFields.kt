package dev.robercoding.decimal.formatter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal
import dev.robercoding.decimal.formatter.price.DecimalTextFieldComposable
import dev.robercoding.decimal.formatter.price.OutlinedDecimalTextFieldComposable

@Composable
internal fun HeaderWithTextFields(
    header: String,
    value: FormattedDecimal?,
    onValueChange: (FormattedDecimal) -> Unit,
    decimalFormatter: DecimalFormatter,
) {
    Column {
        Header(
            text = header,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedDecimalTextFieldComposable(
            currentAmount = value ?: decimalFormatter.createEmptyValue(),
            onValueChange = onValueChange,
            decimalFormatter = decimalFormatter
        )
        Spacer(modifier = Modifier.height(12.dp))
        DecimalTextFieldComposable(
            currentAmount = value ?: decimalFormatter.createEmptyValue(),
            onValueChange = onValueChange,
            decimalFormatter = decimalFormatter
        )
    }
}
