package com.github.terrakok.flowmarbles

import kotlinx.browser.window

actual var browserUrlFragment: String
    get() = window.location.hash.substringAfter("#/", "")
    set(value) {
        val current = window.location.hash.substringAfter("#/", "")
        if (current != value) {
            window.location.hash = "#/$value"
            }
    }

actual fun listenBrowserNavigation(onOpen: (String) -> Unit) {
    window.onhashchange = {
        onOpen(window.location.hash.substringAfter("#/", ""))
    }
}