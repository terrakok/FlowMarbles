package com.github.terrakok.flowmarbles

actual var browserUrlFragment: String = ""
actual fun listenBrowserNavigation(onOpen: (String) -> Unit) = Unit