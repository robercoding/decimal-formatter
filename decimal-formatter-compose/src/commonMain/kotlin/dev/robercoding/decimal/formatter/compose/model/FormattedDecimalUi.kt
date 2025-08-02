// package dev.robercoding.decimal.formatter.compose.model
//
// import com.ionspin.kotlin.bignum.decimal.BigDecimal
// import dev.robercoding.decimal.formatter.core.model.FormattedDecimal
//
// /**
//  * UI-specific wrapper around FormattedNumber that adds prefix support and raw digits.
//  * Delegates all numeric conversion methods to the underlying FormattedNumber.
//  *
//  * @property rawDigits The raw digits only (e.g., "123456")
//  * @property formattedDecimal The core formatted number containing display and parseable values
//  * @property fullDisplay The formatted decimal with optional prefix (e.g., "$1.234,56")
//  */
// data class FormattedDecimalUi internal constructor(
//     val rawDigits: String,
//     private val formattedDecimal: FormattedDecimal,
//     val fullDisplay: String?
// ) {
//     /**
//      * The formatted decimal without prefix (e.g., "1.234,56")
//      */
//     val display: String get() = formattedDecimal.displayValue
//
//     /**
//      * The canonical string that can be parsed by numeric types (e.g., "1234.56")
//      */
//     val parseableValue: String get() = formattedDecimal.parseableValue
//
//     // Delegate all conversion methods to FormattedNumber
//     fun toBigDecimalOrNull(): BigDecimal? = formattedDecimal.toBigDecimalOrNull()
//     fun toBigDecimal(): BigDecimal = formattedDecimal.toBigDecimal()
//     fun toDoubleOrNull(): Double? = formattedDecimal.toDoubleOrNull()
//     fun toDouble(): Double = formattedDecimal.toDouble()
//     fun toFloatOrNull(): Float? = formattedDecimal.toFloatOrNull()
//     fun toFloat(): Float = formattedDecimal.toFloat()
//
//     /**
//      * Returns the display value (without prefix) when used as a string
//      */
//     override fun toString(): String = display
// }
//
