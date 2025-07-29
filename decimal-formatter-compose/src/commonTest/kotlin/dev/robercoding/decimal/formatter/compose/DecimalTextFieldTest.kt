package dev.robercoding.decimal.formatter.compose

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
import dev.robercoding.decimal.formatter.compose.components.DecimalTextField
import dev.robercoding.decimal.formatter.compose.model.FormattedDecimalValue
import dev.robercoding.decimal.formatter.core.DecimalFormatterConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// Execute android test emulator: ./gradlew decimal-formatter-compose:connectedAndroidTest
// Execute iOS test emulator: ./gradlew decimal-formatter-compose:iosSimulatorArm64Test
class DecimalTextFieldTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesCorrectFullDisplayAndDeveloperGetsExpectedDataUs() = runComposeUiTest {
        var currentValue by mutableStateOf("")
        var decimalValueResult: FormattedDecimalValue? = null

        setContent {
            DecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                onDecimalValueChange = { decimalValueResult = it },
                configuration = DecimalFormatterConfiguration.us(),
                prefix = "$",
                modifier = Modifier.testTag("decimal_field")
            )
        }

        onNodeWithTag("decimal_field").performTextInput("2999")

        waitUntil(timeoutMillis = 5000) {
            decimalValueResult?.rawDigits == "2999"
        }

        // ✅ User sees prefix in display
        onNodeWithTag("decimal_field").assertTextEquals("$29.99")

        // ✅ Developer gets structured data
        assertEquals("2999", currentValue)
        assertNotNull(decimalValueResult)
        assertEquals("2999", decimalValueResult.rawDigits)
        assertEquals("29.99", decimalValueResult.display)
        assertEquals("$29.99", decimalValueResult.fullDisplay)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesCorrectFullDisplayAndDeveloperGetsExpectedDataEuropean() = runComposeUiTest {
        var currentValue by mutableStateOf("")
        var decimalValueResult: FormattedDecimalValue? = null

        setContent {
            DecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                onDecimalValueChange = { decimalValueResult = it },
                configuration = DecimalFormatterConfiguration.european(),
                prefix = "€",
                modifier = Modifier.testTag("decimal_field")
            )
        }

        onNodeWithTag("decimal_field").performTextInput("2999")

        waitUntil(timeoutMillis = 5000) {
            decimalValueResult?.rawDigits == "2999"
        }

        // ✅ User sees prefix in display
        onNodeWithTag("decimal_field").assertTextEquals("€29,99")

        // ✅ Developer gets structured data
        assertEquals("2999", currentValue) // Raw for state management
        assertNotNull(decimalValueResult)
        assertEquals("2999", decimalValueResult.rawDigits)
        assertEquals("29,99", decimalValueResult.display)
        assertEquals("€29,99", decimalValueResult.fullDisplay)
    }

    // ===== 2. DIFFERENT DECIMAL FORMATTER CONFIGURATIONS =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesCorrectFormattingForDifferentDecimalFormatterConfigurationOnSwitch() = runComposeUiTest {
        var isEuropean by mutableStateOf(false)
        var currentValue by mutableStateOf("")
        var decimalValueResult: FormattedDecimalValue? = null

        setContent {
            Column {
                DecimalTextField(
                    value = currentValue,
                    onValueChange = { currentValue = it },
                    onDecimalValueChange = { decimalValueResult = it },
                    configuration = if (isEuropean) {
                        DecimalFormatterConfiguration.european()
                    } else {
                        DecimalFormatterConfiguration.us()
                    },
                    modifier = Modifier.testTag("decimal_field")
                )

                Button(
                    onClick = { isEuropean = !isEuropean },
                    modifier = Modifier.testTag("switch_locale")
                ) {
                    Text("Switch Locale")
                }
            }
        }

        // User types in US format
        onNodeWithTag("decimal_field").performTextInput("123456")

        waitUntil(timeoutMillis = 5000) {
            decimalValueResult?.rawDigits == "123456"
        }

        onNodeWithTag("decimal_field").assertTextEquals("1,234.56") // US format
        assertEquals("1,234.56", decimalValueResult!!.display)

        // User switches to European format
        onNodeWithTag("switch_locale").performClick()
        waitForIdle()

        // Same raw data, different formatting
        onNodeWithTag("decimal_field").assertTextEquals("1.234,56") // European format
        assertEquals("123456", currentValue) // Raw data unchanged
        assertEquals("1.234,56", decimalValueResult!!.display) // Format changed
    }

    // ===== 3. PROGRESSIVE INPUT (User typing experience) =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun userSeesProgressiveFormattingWhileTyping() = runComposeUiTest {
        var currentValue by mutableStateOf("")
        var decimalValueResult: FormattedDecimalValue? = null

        setContent {
            DecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                onDecimalValueChange = { decimalValueResult = it },
                configuration = DecimalFormatterConfiguration.us(),
                modifier = Modifier.testTag("decimal_field")
            )
        }

        val testCases = listOf(
            "1" to "0.01",
            "12" to "0.12",
            "123" to "1.23",
            "1234" to "12.34",
            "12345" to "123.45",
            "123456" to "1,234.56"
        )

        testCases.forEach { (input, expectedDisplay) ->
            onNodeWithTag("decimal_field").performTextClearance()
            onNodeWithTag("decimal_field").performTextInput(input)

            waitUntil(timeoutMillis = 5000) {
                decimalValueResult?.rawDigits == input
            }

            // User sees progressive formatting
            onNodeWithTag("decimal_field").assertTextEquals(expectedDisplay)

            // Developer gets consistent raw data
            assertEquals(input, currentValue)
            assertEquals(input, decimalValueResult!!.rawDigits)
            assertEquals(expectedDisplay, decimalValueResult!!.display)
        }
    }

    // ===== 4. STATE SYNCHRONIZATION =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun componentSyncsStateChanges() = runComposeUiTest {
        var currentValue by mutableStateOf("123")
        var decimalValueResult: FormattedDecimalValue? = null

        setContent {
            Column {
                DecimalTextField(
                    value = currentValue,
                    onValueChange = { currentValue = it },
                    onDecimalValueChange = { decimalValueResult = it },
                    configuration = DecimalFormatterConfiguration.us(),
                    modifier = Modifier.testTag("decimal_field")
                )

                Button(
                    onClick = { currentValue = "56789" },
                    modifier = Modifier.testTag("update_button")
                ) {
                    Text("Update Value")
                }

                Button(
                    onClick = { currentValue = "" },
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
            decimalValueResult?.rawDigits == "56789"
        }

        // User sees updated formatted value
        onNodeWithTag("decimal_field").assertTextEquals("567.89")
        assertEquals("56789", currentValue)

        // Parent clears value
        onNodeWithTag("clear_button").performClick()
        waitForIdle()

        // User sees empty state
        onNodeWithTag("decimal_field").assertTextEquals("0.00")
        assertEquals("", currentValue)
    }

    // ===== 5. EDGE CASES & INPUT VALIDATION =====

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun componentHandlesInvalidInputGracefully() = runComposeUiTest {
        var currentValue by mutableStateOf("")
        var decimalValueResult: FormattedDecimalValue? = null

        setContent {
            DecimalTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
                onDecimalValueChange = { decimalValueResult = it },
                configuration = DecimalFormatterConfiguration.us(),
                modifier = Modifier.testTag("decimal_field")
            )
        }

        val testCases = listOf(
            "abc123def" to ("123" to "1.23"), // Letters filtered out
            "..1234.." to ("1234" to "12.34"), // Special chars filtered
            "000123" to ("123" to "1.23"), // Leading zeros removed
            "" to ("" to "0.00") // Empty input
        )

        testCases.forEach { (input, expected) ->
            val expectedRawDigits = expected.first
            val expectedDisplay = expected.second
            onNodeWithTag("decimal_field").performTextClearance()
            onNodeWithTag("decimal_field").performTextInput(input)

            waitUntil(timeoutMillis = 5000) {
                decimalValueResult?.rawDigits == expected.first
            }

            // User sees clean formatted output
            onNodeWithTag("decimal_field").assertTextEquals(expectedDisplay)

            // Developer gets cleaned raw data
            assertEquals(input, currentValue)
            assertEquals(expectedRawDigits, decimalValueResult!!.rawDigits)
        }
    }
}
