package dev.robercoding.decimal.formatter.utils

import co.touchlab.kermit.Logger
import dev.robercoding.decimal.formatter.DecimalFormatterDebugConfig

fun logMessage(message: String) {
    if (!DecimalFormatterDebugConfig.isDebugEnabled) return

    Logger.i(tag = "DecimalFormatter", messageString = message)
}
