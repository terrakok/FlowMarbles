package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.theme.AppTheme
import com.github.terrakok.flowmarbles.theme.Icons
import kotlinx.coroutines.launch

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    var selectedOperator by remember {
        mutableStateOf(allOperators.entries.first().value.first())
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val windowInfo = LocalWindowInfo.current
    val isCompact = windowInfo.containerDpSize.width < 800.dp

    if (isCompact) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    SidePanel(
                        selectedOperator = selectedOperator,
                        onOperatorSelected = {
                            coroutineScope.launch {
                                selectedOperator = it
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            },
        ) {
            OperatorContent(
                selectedOperator = selectedOperator,
                showMenuButton = true,
                onOpenMenuClick = {
                    coroutineScope.launch { drawerState.open() }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            SidePanel(
                selectedOperator = selectedOperator,
                onOperatorSelected = { selectedOperator = it },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(340.dp)
            )
            VerticalDivider()
            OperatorContent(
                selectedOperator = selectedOperator,
                showMenuButton = false,
                onOpenMenuClick = {},
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

@Composable
fun OperatorContent(
    selectedOperator: Operator,
    showMenuButton: Boolean,
    onOpenMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showMenuButton) {
                IconButton(
                    onClick = onOpenMenuClick,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(Icons.Menu, contentDescription = "Open menu")
                }
            }
            Text(
                text = "# " + selectedOperator.name,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

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
