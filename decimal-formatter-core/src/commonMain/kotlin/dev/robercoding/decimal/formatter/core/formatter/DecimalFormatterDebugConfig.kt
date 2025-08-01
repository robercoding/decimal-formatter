package dev.robercoding.decimal.formatter.core.formatter

import kotlin.concurrent.Volatile

object DecimalFormatterDebugConfig {
    @Volatile
    var isDebugEnabled: Boolean = false
        private set
    
    fun setDebugEnabled(enabled: Boolean) {
        isDebugEnabled = enabled
    }
    
    fun toggleDebug() {
        isDebugEnabled = !isDebugEnabled
    }
}