package com.github.terrakok.flowmarbles

import kotlinx.browser.window

actual fun getBrowserUrlFragment(): String =
    window.location.hash.substringAfter("#/", "")

actual fun updateBrowserUrlFragment(fragment: String) {
    window.location.hash = "#/$fragment"
}