package dev.robercoding.decimal.formatter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.robercoding.decimal.formatter.percentage.PercentageSection
import dev.robercoding.decimal.formatter.price.PriceSection
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        val viewModel = remember { DecimalFormatterViewModel() }
        val state by viewModel.uiState.collectAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 32.dp).verticalScroll(rememberScrollState())
        ) {
            PercentageSection(
                inputMode = state.percentageInputMode,
                onInputModeChange = { newMode -> viewModel.setPercentageInputMode(newMode) }
            )
            WeightSection(
                inputMode = state.weightInputMode,
                onInputModeChange = { newMode -> viewModel.setWeightInputMode(newMode) }
            )
            PriceSection(
                priceEuro = state.priceEuro,
                priceEuropean = state.priceEuropean,
                inputMode = state.priceInputMode,
                onPriceEuroChange = { newPrice -> viewModel.setPriceEuro(newPrice) },
                onPriceEuropeanChange = { newPrice -> viewModel.setPriceEuropean(newPrice) },
                onInputModeChange = { newMode -> viewModel.setPriceInputMode(newMode) }
            )
        }
    }
}
