package dev.robercoding.decimal.formatter.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatter
import dev.robercoding.decimal.formatter.core.formatter.DecimalFormatterConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

// Execute android test emulator: ./gradlew decimal-formatter-compose:connectedAndroidTest
// Execute iOS test emulator: ./gradlew decimal-formatter-compose:iosSimulatorArm64Test
class DecimalTextFieldTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesCorrectFullDisplayAndDeveloperGetsExpectedDataUs() = runComposeUiTest {
        val decimalFormatter = DecimalFormatter(DecimalFormatterConfiguration.usd())
        var currentValue by mutableStateOf(decimalFormatter.format(""))

        setContent {
            OutlinedDecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                decimalFormatter = decimalFormatter,
                modifier = Modifier.testTag("decimal_field")
            )
        }

        onNodeWithTag("decimal_field").performTextInput("2999")

        waitUntil(timeoutMillis = 5000) {
            currentValue.rawDigits == "2999"
        }

        // ✅ User sees prefix in display
        onNodeWithTag("decimal_field").assertTextEquals("$29.99")

        // ✅ Developer gets structured data
        assertEquals("2999", currentValue.rawDigits)
        assertEquals("29.99", currentValue.parseableValue)
        assertEquals("$29.99", currentValue.displayValue)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesCorrectFullDisplayAndDeveloperGetsExpectedDataEuropean() = runComposeUiTest {
        val decimalFormatter = DecimalFormatter(DecimalFormatterConfiguration.euro())
        var currentValue by mutableStateOf(decimalFormatter.format(""))

        setContent {
            OutlinedDecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                decimalFormatter = decimalFormatter,
                modifier = Modifier.testTag("decimal_field")
            )
        }

        onNodeWithTag("decimal_field").performTextInput("2999")

        waitUntil(timeoutMillis = 5000) {
            currentValue.rawDigits == "2999"
        }

        // ✅ User sees prefix in display
        onNodeWithTag("decimal_field").assertTextEquals("29,99 €")

        // ✅ Developer gets structured data
        assertEquals("2999", currentValue.rawDigits)
        assertEquals("29.99", currentValue.parseableValue)
        assertEquals("29,99 €", currentValue.displayValue)
    }

    // ===== 2. DIFFERENT DECIMAL FORMATTER CONFIGURATIONS =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesCorrectFormattingForDifferentDecimalFormatterConfigurationOnSwitch() = runComposeUiTest {
        var isEuropean by mutableStateOf(false)

        val usFormatter = DecimalFormatter(DecimalFormatterConfiguration.us())
        val europeanFormatter = DecimalFormatter(DecimalFormatterConfiguration.european())

        var currentValue by mutableStateOf(usFormatter.format("123456"))

        setContent {
            Column {
                OutlinedDecimalTextField(
                    value = currentValue,
                    onValueChange = { currentValue = it },
                    decimalFormatter = if (isEuropean) europeanFormatter else usFormatter,
                    modifier = Modifier.testTag("decimal_field")
                )

                Button(
                    onClick = {
                        isEuropean = !isEuropean
                        // Reformat existing data with new formatter
                        val newFormatter = if (isEuropean) europeanFormatter else usFormatter
                        currentValue = newFormatter.format(currentValue.rawDigits)
                    },
                    modifier = Modifier.testTag("switch_locale")
                ) {
                    Text("Switch Locale")
                }
            }
        }

        // User sees initial US format
        onNodeWithTag("decimal_field").assertTextEquals("1,234.56")
        assertEquals("1,234.56", currentValue.displayValue)
        assertEquals("1234.56", currentValue.parseableValue) // Format changed
        assertEquals("123456", currentValue.rawDigits) // Raw data unchanged

        // User switches to European format
        onNodeWithTag("switch_locale").performClick()
        waitForIdle()

        // Same raw data, different formatting
        onNodeWithTag("decimal_field").assertTextEquals("1.234,56") // European format
        assertEquals("123456", currentValue.rawDigits) // Raw data unchanged
        assertEquals("1234.56", currentValue.parseableValue) // Format changed
        assertEquals("1.234,56", currentValue.displayValue) // Format changed
    }

    // ===== 3. PROGRESSIVE INPUT (User typing experience) =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesProgressiveFormattingWhileTyping() = runComposeUiTest {
        val usFormatter = DecimalFormatter(DecimalFormatterConfiguration.us())
        var currentValue by mutableStateOf(usFormatter.format(""))

        setContent {
            OutlinedDecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                decimalFormatter = usFormatter,
                modifier = Modifier.testTag("decimal_field")
            )
        }

        // Triple(input, expectedParseableValue, expectedDisplayValue)
        val testCases = listOf(
            Triple("1", "0.01", "0.01"),
            Triple("12", "0.12", "0.12"),
            Triple("123", "1.23", "1.23"),
            Triple("1234", "12.34", "12.34"),
            Triple("12345", "123.45", "123.45"),
            Triple("123456", "1234.56", "1,234.56")
        )

        testCases.forEach { (input, expectedParseable, expectedDisplay) ->
            onNodeWithTag("decimal_field").performTextClearance()
            onNodeWithTag("decimal_field").performTextInput(input)

            waitUntil(timeoutMillis = 5000) {
                currentValue.rawDigits == input
            }

            // User sees progressive formatting in the UI
            onNodeWithTag("decimal_field").assertTextEquals(expectedDisplay)

            // Developer gets consistent data from the FormattedDecimal object
            assertEquals(input, currentValue.rawDigits, "Raw digits should match input for: $input")
            assertEquals(expectedParseable, currentValue.parseableValue, "Parseable value should be correct for input: $input")
            assertEquals(expectedDisplay, currentValue.displayValue, "Display value should be correct for input: $input")
        }
    }


    // ===== 4. STATE SYNCHRONIZATION =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun componentSyncsStateChanges() = runComposeUiTest {
        val usFormatter = DecimalFormatter(DecimalFormatterConfiguration.us())
        var currentValue by mutableStateOf(usFormatter.format("123"))

        setContent {
            Column {
                OutlinedDecimalTextField(
                    value = currentValue,
                    onValueChange = { currentValue = it },
                    decimalFormatter = usFormatter,
                    modifier = Modifier.testTag("decimal_field")
                )

                Button(
                    onClick = { currentValue = usFormatter.format("56789") },
                    modifier = Modifier.testTag("update_button")
                ) {
                    Text("Update Value")
                }

                Button(
                    onClick = { currentValue = usFormatter.format("") },
                    modifier = Modifier.testTag("clear_button")
                ) {
                    Text("Clear")
                }
            }
        }

        // User sees initial formatted value
        onNodeWithTag("decimal_field").assertTextEquals("1.23")

        // Parent updates value programmatically
        onNodeWithTag("update_button").performClick()

        waitUntil(timeoutMillis = 5000) {
            currentValue.rawDigits == "56789"
        }

        // User sees updated formatted value
        onNodeWithTag("decimal_field").assertTextEquals("567.89")
        assertEquals("56789", currentValue.rawDigits)

        // Parent clears value
        onNodeWithTag("clear_button").performClick()
        waitForIdle()

        // User sees empty state
        onNodeWithTag("decimal_field").assertTextEquals("0.00")
        assertEquals("", currentValue.rawDigits)
    }

    // ===== 5. EDGE CASES & INPUT VALIDATION =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun componentHandlesInvalidInputGracefullySimple() = runComposeUiTest {
        val usFormatter = DecimalFormatter(DecimalFormatterConfiguration.us())
        var currentValue by mutableStateOf(usFormatter.format(""))

        setContent {
            OutlinedDecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                decimalFormatter = usFormatter,
                modifier = Modifier.testTag("decimal_field")
            )
        }

        val testCases = listOf(
            "abc123def" to Triple("123", "1.23", "1.23"), // Letters filtered out
            "..1234.." to Triple("1234", "12.34", "12.34"), // Special chars filtered
            "000123" to Triple("123", "1.23", "1.23"), // Leading zeros removed
            "" to Triple("", "0.00", "0.00"), // Empty input
            "$#@!" to Triple("", "0.00", "0.00"), // No digits
            "1a2b3c4d5e6f" to Triple("123456", "1234.56", "1,234.56") // Mixed with thousand separator
        )

        testCases.forEach { (input, expected) ->
            val expectedRawDigits = expected.first
            val expectedParseable = expected.second
            val expectedDisplay = expected.third

            onNodeWithTag("decimal_field").performTextClearance()
            onNodeWithTag("decimal_field").performTextInput(input)

            waitUntil(timeoutMillis = 5000) {
                currentValue.rawDigits == expectedRawDigits
            }

            onNodeWithTag("decimal_field").assertTextEquals(expectedDisplay)

            assertEquals(expectedRawDigits, currentValue.rawDigits)
            assertEquals(expectedParseable, currentValue.parseableValue)
            assertEquals(expectedDisplay, currentValue.displayValue)
        }
    }
}