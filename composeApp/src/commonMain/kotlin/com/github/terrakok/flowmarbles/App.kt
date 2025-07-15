package com.github.terrakok.flowmarbles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.theme.AppTheme
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun App() = AppTheme {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val minW = with(LocalDensity.current) { 750.dp.roundToPx() }
        val maxW = with(LocalDensity.current) { 1000.dp.roundToPx() }
        var containerWidth by remember { mutableStateOf(minW) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { containerWidth = it.size.width }
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState())
                .layout { m, c ->
                    val minC = containerWidth.coerceIn(minW, maxW)
                    val placeable = m.measure(
                        c.copy(minWidth = minC, minHeight = 0, maxWidth = minC, maxHeight = Int.MAX_VALUE)
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }
                .padding(40.dp)
        ) {
            Text(
                text = "Flow Marbles",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Interactive diagrams of Kotlinx.coroutines Flow",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row {
                var showFilter by remember { mutableStateOf(true) }
                var showDrop by remember { mutableStateOf(false) }
                var showTake by remember { mutableStateOf(false) }
                var showMerge by remember { mutableStateOf(true) }
                var showCombine by remember { mutableStateOf(false) }
                var showZip by remember { mutableStateOf(false) }

                Column {
                    TextButton(
                        onClick = { showFilter = !showFilter },
                        border = if (showFilter) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text("filter")
                    }
                    TextButton(
                        onClick = { showDrop = !showDrop },
                        border = if (showDrop) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text("drop")
                    }
                    TextButton(
                        onClick = { showTake = !showTake },
                        border = if (showTake) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text("take")
                    }
                    TextButton(
                        onClick = { showMerge = !showMerge },
                        border = if (showMerge) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) { Text("merge") }
                    TextButton(
                        onClick = { showCombine = !showCombine },
                        border = if (showCombine) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) { Text("combine") }
                    TextButton(
                        onClick = { showZip = !showZip },
                        border = if (showZip) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) { Text("zip") }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (showFilter) FlowFilter()
                    if (showDrop) FlowDrop()
                    if (showTake) FlowTake()
                    if (showMerge) FlowMerge()
                    if (showCombine) FlowCombine()
                    if (showZip) FlowZip()
                }
            }
        }
    }
}
