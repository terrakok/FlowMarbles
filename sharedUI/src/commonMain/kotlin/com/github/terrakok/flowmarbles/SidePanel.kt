package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.theme.DarkColorScheme
import com.github.terrakok.flowmarbles.theme.LocalThemeIsDark

@Composable
fun SidePanel(
    selectedOperator: Operator,
    onOperatorSelected: (Operator) -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme
    ) {
        Surface {
            Column(modifier) {
                AppHeader(
                    modifier = Modifier.padding(16.dp)
                )
                var currentTheme by LocalThemeIsDark.current
                ThemeSwitcher(
                    selectedMode = ThemeMode.fromBoolean(currentTheme),
                    onModeSelected = { mode ->
                        currentTheme = mode.toBoolean()
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                OperatorsList(
                    selected = selectedOperator,
                    onOperatorSelected = onOperatorSelected,
                    modifier = Modifier.weight(1f)
                )
                HorizontalDivider()

                val uriHandler = LocalUriHandler.current
                AppFooter(
                    onGitHubClick = { uriHandler.openUri("https://github.com/terrakok/FlowMarbles") },
                    onSupportClick = { uriHandler.openUri("https://www.buymeacoffee.com/terrakok") },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
