# Decimal Formatter 🔢

[![Maven Central](https://img.shields.io/maven-central/v/dev.robercoding/decimal-formatter-core)](https://search.maven.org/search?q=g:dev.robercoding%20AND%20a:decimal-formatter-*)  
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform-blue)](https://kotlinlang.org/docs/multiplatform.html)  
[![Compose](https://img.shields.io/badge/compose-multiplatform-green)](https://www.jetbrains.com/lp/compose-multiplatform/)  
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A Kotlin Multiplatform library for decimal number formatting with automatic input formatting and Jetpack Compose UI components.

Perfect for financial apps, calculators, and any application that needs professional decimal number input and display.

## ✨ Features

- 🌍 **Kotlin Multiplatform** - Works on Android, iOS, and more
- 🎨 **Jetpack Compose** - Ready-to-use UI components
- 💰 **Currency Support** - Built-in prefix support for currency symbols
- 🌎 **International Formats** - European, US, Swiss, and custom formats
- ⚡ **Real-time Formatting** - Automatic formatting as users type
- 🧩 **Modular Design** - Use core logic only or with UI components
- 🔧 **Highly Configurable** - Decimal places, separators, max digits
- 📊 **Structured Data** - Get raw digits, formatted display, and full formatted display
- 🎚️ **Two Input Modes** - FRACTIONAL (cents mode) or FIXED_DECIMALS (dollar mode) with zero-padding

## 📦 Installation

### Core Module (Platform Agnostic)
```kotlin  
dependencies {  
    implementation("dev.robercoding:decimal-formatter-core:0.0.1-alpha01")
}  
```  

### Compose Module (UI Components)
```kotlin  
dependencies {
    implementation("dev.robercoding:decimal-formatter-compose:0.0.1-alpha01")
    // Note: This automatically includes the core module
}  
```  

## 🚀 Quick Start

### Basic Usage (Core)

```kotlin   
// Create a formatter
val formatter = DecimalFormatter(DecimalFormatterConfiguration.european())  
  
// Format numbers  
val formatted = formatter.format("123456") // "1.234,56"
```  

### Compose UI Components

The new API uses `UiDecimalFormatter` for better encapsulation and `DecimalValue` for structured state management:

```kotlin  
@Composable
fun CurrencyInput() {
    // Create a UI formatter with prefix
    val europeanFormatter = rememberUiDecimalFormatter(configuration = DecimalFormatterConfiguration.european(), prefix = "€")

    // Use DecimalValue for structured state
    var price by remember {
        mutableStateOf(europeanFormatter.format("")) // Start with empty if needed, will format to "€0,00"
    }

    OutlinedDecimalTextField(
        decimalValue = price,
        onValueChange = { price = it },
        decimalFormatter = europeanFormatter,
        label = { Text("Price") }
    )
}

@Composable
fun WeightInput() {
    // Custom configuration without prefix
    val decimalFormatter = remember {
        UiDecimalFormatter(
            decimalFormatter = DecimalFormatter(
                DecimalFormatterConfiguration(
                    decimalSeparator = DecimalSeparator.DOT,
                    thousandSeparator = ThousandSeparator.COMMA,
                    decimalPlaces = 3,
                    maxDigits = 10
                )
            ),
            prefix = null
        )
    }

    var weight by remember {
        mutableStateOf(decimalFormatter.format("123456")) // Initialize with value, will format to "123,456"
    }

    OutlinedDecimalTextField(
        decimalValue = weight,
        onValueChange = { weight = it },
        decimalFormatter = decimalFormatter,
        label = { Text("Kilograms") }
    )
}

@Composable
fun DynamicFormatterExample() {
    var isEuropean by remember { mutableStateOf(false) }

    val usFormatter = rememberUiDecimalFormatter(DecimalFormatterConfiguration.us(), prefix = "$")
    val europeanFormatter = rememberUiDecimalFormatter(DecimalFormatterConfiguration.european(), prefix = "€")

    var amount by remember { mutableStateOf(usFormatter.format("123456")) }

    Column {
        OutlinedDecimalTextField(
            decimalValue = amount,
            onValueChange = { amount = it },
            decimalFormatter = if (isEuropean) europeanFormatter else usFormatter
        )

        Button(
            onClick = {
                isEuropean = !isEuropean
                // Reformat existing data with new formatter
                val newFormatter = if (isEuropean) europeanFormatter else usFormatter
                amount = newFormatter.format(amount.rawDigits)
            }
        ) {
            Text("Switch Format")
        } 
    }
}
```  

## 🎯 Components

### Core Module Components

| Component | Description |
|-----------|-------------|
| `DecimalFormatter` | Core formatting logic |
| `DecimalFormatterConfiguration` | Formatting configuration |
| `DecimalSeparator` / `ThousandSeparator` | Separator enums |
| `DecimalInputMode` | Input mode enum (FRACTIONAL or FIXED_DECIMALS) |  

### Compose Module Components

| Component | Description |  
|-----------|-------------|  
| `OutlinedDecimalTextField` | Material 3 outlined text field |  
| `UiDecimalFormatter` | UI-layer formatter with prefix support |
| `DecimalValue` | Structured data class for formatted values |

## 🏗️ DecimalValue Structure

The `DecimalValue` data class provides structured access to formatted data:

```kotlin
data class DecimalValue internal constructor(
    val rawDigits: String,      // "123456" - pure digits for any calculation you need
    val display: String,        // "1.234,56" - formatted without prefix  
    val fullDisplay: String?    // "€1.234,56" - formatted with prefix. Nullable if you didn't provide a prefix
)

// Note: You can only create `DecimalValue` using `UiDecimalFormatter.format()`
// Usage examples
val value = uiDecimalFormatter.format("123456")
println(value.rawDigits)    // "123456"
println(value.display)      // "1.234,56" -> Can vary depending on the type of formatter used
println(value.fullDisplay)  // "€1.234,56" -> Can vary depending on the type of formatter used.

// Use display for showing to users  
Text(value.fullDisplay ?: value.display)
```

## ⚙️ Configuration

### Built-in Formats

```kotlin  
// European: 1.234,56  
DecimalFormatterConfiguration.european()

// US: 1,234.56
DecimalFormatterConfiguration.us()

// Swiss: 1'234.56  
DecimalFormatterConfiguration.swiss()

// Plain: 1234.56 (no thousand separators)  
DecimalFormatterConfiguration.plain()
```  

### Custom Configuration

Any configuration can be customized using `DecimalFormatterConfiguration`:

```kotlin  
DecimalFormatterConfiguration(  
    decimalSeparator = DecimalSeparator.COMMA,
    thousandSeparator = ThousandSeparator.SPACE,
    decimalPlaces = 3,
    maxDigits = 10
)  
```

### Input Validation & Cleaning
```kotlin
// The formatter automatically handles invalid input
formatter.format("abc123def")  // Filters the letters
formatter.format("$1,234.56")  // Extracts digits → "123456" → "1,234.56"
formatter.format("000123")     // Removes leading zeros → "123" → "1.23"
```

## 🎚️ Input Modes

Choose between two input interpretation modes:

### FRACTIONAL Mode (Default - "Cents Mode")

Traditional behavior where digits fill decimal places from the right. Perfect for cash registers and financial apps.

```kotlin
val formatter = DecimalFormatter(
    DecimalFormatterConfiguration.us(
        decimalPlaces = 2,
        inputMode = DecimalInputMode.FRACTIONAL  // Default
    )
)

formatter.format("1")       // "0.01" (1 cent)
formatter.format("123")     // "1.23" ($1.23)
formatter.format("123456")  // "1,234.56"
```

### FIXED_DECIMALS Mode ("Dollar Mode")

Treats input as whole numbers and pads decimals with zeros. Also accepts formatted input with decimal separators. Ideal for measurements, weights, and scientific notation.

```kotlin
val formatter = DecimalFormatter(
    DecimalFormatterConfiguration.us(
        decimalPlaces = 3,
        inputMode = DecimalInputMode.FIXED_DECIMALS
    )
)

// Raw digits (no decimal separator)
formatter.format("1")       // "1.000" (1 unit)
formatter.format("14")      // "14.000" (14 units)

// Formatted input (with decimal separator)
formatter.format("1.45")    // "1.450" (pads with zeros)
formatter.format("14.50")   // "14.500"
formatter.format("1,5")     // "1.500" (accepts comma separator)
```

**Key Differences:**

| Feature | FRACTIONAL | FIXED_DECIMALS |
|---------|------------|----------------|
| Input "1" with 2 decimals | "0.01" | "1.00" |
| Input "123" with 2 decimals | "1.23" | "123.00" |
| Accepts decimal separators | ❌ (digits only) | ✅ (. and ,) |
| Trailing zeros | ❌ | ✅ Padded |
| Use case | Currency input | Measurements, weights, scientific |

**Example Use Cases:**

```kotlin
// Weight input with FIXED_DECIMALS
val weightFormatter = DecimalFormatter(
    DecimalFormatterConfiguration.plain(
        decimalPlaces = 2,
        suffix = " kg",
        inputMode = DecimalInputMode.FIXED_DECIMALS
    )
)
weightFormatter.format("75.5")  // "75.50 kg"

// Price input with FRACTIONAL (traditional)
val priceFormatter = DecimalFormatter(
    DecimalFormatterConfiguration.usd(
        inputMode = DecimalInputMode.FRACTIONAL
    )
)
priceFormatter.format("1995")  // "$19.95"
```

## 🐛 Debug Logging

Enable debug logging to troubleshoot formatting issues:

```kotlin  
class MyApplication : Application() {  
    override fun onCreate() {  
        super.onCreate()
        DecimalFormatterDebugConfig.setDebugEnabled(true)
    }
}
```  

## 🏗️ Architecture

This library is split into two modules:

- **`decimal-formatter-core`** - Pure Kotlin logic, works everywhere
- **`decimal-formatter-compose`** - Jetpack Compose UI components

```
decimal-formatter/
└── core/                         # Platform-agnostic formatting
    └── formatter/
        ├── DecimalFormatter         # Core formatting logic
        ├── DecimalFormatterConfiguration # Formatting rules
        └── DecimalFormatterDebugConfig # Debug logging configuration
    └── model/
        ├── DecimalInputMode        # Input mode enum (FRACTIONAL/FIXED_DECIMALS)
        ├── ThousandSeparator       # Enum for thousand separators
        ├── DecimalSeparator        # Enum for decimal separators
        └── FormattedDecimal        # Result object with display/parseable values
    └── utils/
        └── LoggerUtils             # Utility for logging
└── compose/                     # Compose UI components
    └── components/
        ├── DecimalTextField # Basic text field without any decorations
        └── OutlinedDecimalTextField # Material text field
    └── model/
        └── DecimalValue
    └── formatter/
        └── UiDecimalFormatter      # UI-layer formatter with prefix support
```

## 🤝 Contributing

Contributions are welcome! 

## 📄 License

```  
Copyright 2025 Roberto Fuentes  
  
Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at  
  
    http://www.apache.org/licenses/LICENSE-2.0  
    
Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.  
```  

## 🔗 Links

- [Documentation](https://github.com/robercoding/decimal-formatter/wiki)
- [Issue Tracker](https://github.com/robercoding/decimal-formatter/issues)
- [Discussions](https://github.com/robercoding/decimal-formatter/discussions)
- [Maven Central](https://search.maven.org/search?q=g:dev.robercoding%20AND%20a:decimal-formatter-*)

**Made with ❤️ by [Roberto Fuentes](https://github.com/robercoding)**