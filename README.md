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

## 📦 Installation

### Core Module (Platform Agnostic)
```kotlin  
dependencies {  
 implementation("dev.robercoding:decimal-formatter-core:0.0.1-alpha01")}  
```  

### Compose Module (UI Components)
```kotlin  
dependencies {
    implementation("dev.robercoding:decimal-formatter-compose:0.0.1-alpha01")
}// Note: This automatically includes the core module}  
```  

## 🚀 Quick Start

### Basic Usage (Core)

```kotlin   
// Create a formatter  
val formatter = DecimalFormatter(DecimalFormatterConfiguration.european())  
  
// Format numbers  
val formatted = formatter.format("123456") // "1.234,56"  
  
// Parse back to decimal value  
val value = formatted.parseDecimalValue(2) // 1234.56  
```  

### Compose UI Components

The `value` parameter sets the initial display value. The component then manages its internal state automatically, formatting user input in real-time. Use `onValueChange` to receive the formatted results for your app logic.

```kotlin  
@Composable
fun CurrencyInput() {
    var amount by remember { mutableStateOf("123456") }
    
    val configuration = remember { DecimalFormatterConfiguration.european() }

    OutlinedDecimalTextField(
        value = amount,
        onValueChange = { processed ->
            // processed.raw // Access raw value: "123456"
            // processed.formatted // Access formatted value: "1.234,56"  
            // processed.formattedWithPrefix // Access formatted value with prefix: "€ 1.234,56"
            amount = processed.formatted
        },
        prefix = "€ ",
        configuration = configuration,
        label = { Text("Price") }
    )
}


@Composable
fun WeightInput() {
    var amount by remember { mutableStateOf("123456") }

    val configuration = remember { 
        DecimalFormatterConfiguration(
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            decimalPlaces = 3,
            maxDigits = 10
        ) 
    }


    OutlinedDecimalTextField(
        value = amount,
        onValueChange = { processed ->
            // processed.raw // Access raw value: "123456"
            // processed.formatted // Access formatted value: "123,456  
            // processed.formattedWithPrefix // Access formatted value with prefix: "123,456"
            amount = processed.formatted
        },
        prefix = null,
        configuration = configuration,
        label = { Text("Kilograms") }
    )
}
```  

## 🎯 Components

### Core Module Components

| Component | Description |  
|-----------|-------------|  
| `DecimalFormatter` | Core formatting logic |  
| `DecimalFormatterConfiguration` | Formatting configuration |  
| `DecimalSeparator` / `ThousandSeparator` | Separator enums |  

### Compose Module Components

| Component | Description |  
|-----------|-------------|  
| `OutlinedDecimalTextField` | Material 3 outlined text field |  
| `BasicDecimalTextField` | Unstyled text field |    

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

```kotlin  
DecimalFormatterConfiguration(  
  decimalSeparator = DecimalSeparator.COMMA, thousandSeparator = ThousandSeparator.SPACE, decimalPlaces = 3, maxDigits = 10
)  
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
├── core/                          # Platform-agnostic formatting  
│   ├── DecimalFormatter          # Main formatting class 
│   ├── DecimalFormatterConfiguration # Configuration  
└── compose/                      # Compose UI components  
 ├── OutlinedDecimalTextField  # Material text field 
 ├── BasicDecimalTextField     # Unstyled text field 
```  

**From Manual Formatting:**
```kotlin  
// Before: Manual string manipulation  
fun formatCurrency(amount: String): String {
    // Complex formatting logic...}  

// After: Use DecimalFormatter  
    val formatter = DecimalFormatter.us()
    val formatted = formatter.format(amount)
}
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