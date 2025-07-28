package dev.robercoding.decimal.shared

import androidx.compose.ui.window.ComposeUIViewController
import dev.robercoding.decimal.formatter.App
import platform.UIKit.UIViewController

public fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}
