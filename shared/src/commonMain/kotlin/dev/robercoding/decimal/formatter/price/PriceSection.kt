package dev.robercoding.decimal.formatter.price

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import dev.robercoding.decimal.formatter.compose.components.DecimalTextField
import dev.robercoding.decimal.formatter.compose.components.OutlinedDecimalTextField
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal

@Composable
fun PriceSection(
    priceEuropean: FormattedDecimal?, // VM example
    priceEuro: FormattedDecimal?, // VM example
    onPriceEuroChange: (FormattedDecimal) -> Unit,
    onPriceEuropeanChange: (FormattedDecimal) -> Unit,
) {
    Column {
        EuSection(
            priceEuro = priceEuro,
            priceEuropean = priceEuropean,
            onPriceEuroChange = onPriceEuroChange,
            onPriceEuropeanChange = onPriceEuropeanChange,
        )
        UsSection()
        SwissSection()
    }
}

@Composable
fun OutlinedDecimalTextFieldComposable(
    currentAmount: FormattedDecimal,
    onValueChange: (FormattedDecimal) -> Unit,
    decimalFormatter: DecimalFormatter
) {
    OutlinedDecimalTextField(
        modifier = Modifier,
        value = currentAmount,
        onValueChange = onValueChange,
        label = { Text("Price", style = MaterialTheme.typography.labelSmall) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = if (currentAmount.parseableValue == "0.00") {
                Color.Gray
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        ),
        decimalFormatter = decimalFormatter
    )
}

@Composable
fun DecimalTextFieldComposable(
    modifier: Modifier = Modifier,
    currentAmount: FormattedDecimal,
    onValueChange: (FormattedDecimal) -> Unit,
    decimalFormatter: DecimalFormatter,
) {
    DecimalTextField(
        modifier = Modifier,
        value = currentAmount,
        onValueChange = onValueChange,
        prefix = "$",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.bodyLarge,
        decimalFormatter = decimalFormatter
    )
}