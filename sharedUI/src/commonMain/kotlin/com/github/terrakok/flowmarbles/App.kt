package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.theme.AppTheme

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        var selectedOperator by remember {
            mutableStateOf(allOperators.entries.first().value.first())
        }
        SidePanel(
            selectedOperator = selectedOperator,
            onOperatorSelected = { selectedOperator = it },
            modifier = Modifier
                .fillMaxHeight()
                .width(340.dp)
        )
        VerticalDivider()
        Column(modifier = Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())) {
            Text(
                text = "# " + selectedOperator.name,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(16.dp)
            )

            selectedOperator.content(
                Modifier.padding(horizontal = 16.dp)
            )

            SelectionContainer {
                Text(
                    text = selectedOperator.doc,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
