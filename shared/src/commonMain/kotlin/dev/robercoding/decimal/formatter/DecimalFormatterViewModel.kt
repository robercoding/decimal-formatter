package dev.robercoding.decimal.formatter

import androidx.lifecycle.ViewModel
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import dev.robercoding.decimal.formatter.core.model.FormattedDecimal
import dev.robercoding.decimal.formatter.core.utils.logMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DecimalFormatterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setPriceEuro(price: FormattedDecimal) {
        _uiState.update { it.copy(priceEuro = price) }
    }

    fun setPriceEuropean(price: FormattedDecimal) {
        _uiState.update { it.copy(priceEuropean = price) }
    }
}