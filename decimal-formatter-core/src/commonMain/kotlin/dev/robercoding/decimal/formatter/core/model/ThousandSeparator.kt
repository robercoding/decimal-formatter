package dev.robercoding.decimal.formatter.core.model

/**
 * Enum representing the thousand separator options.
 */
enum class ThousandSeparator(val char: Char?) {
    NONE(null),
    COMMA(','),
    DOT('.'),
    SPACE(' '),
    APOSTROPHE('\'')
}