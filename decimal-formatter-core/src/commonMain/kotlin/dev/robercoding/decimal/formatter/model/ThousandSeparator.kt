package dev.robercoding.decimal.formatter.model

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