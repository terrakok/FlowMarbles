package com.github.terrakok.flowmarbles

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FlowCase(
                    inputFlow = generateEventFlow(color = EventColor.RED),
                    operator = { map { it.copy(color = EventColor.YELLOW) } },
                    label = "map { it.copy(color = YELLOW) }",
                )
                FlowCase(
                    inputFlow = generateEventFlow(),
                    operator = { filter { it.value < 4 } },
                    label = " filter { it.value < 4 }",
                )
                FlowCase(
                    inputFlow = generateEventFlow(color = EventColor.PURPLE),
                    operator = { take(3) },
                    label = " take(3)",
                )
//                FlowCase(
//                    inputFlow = generateEventFlow(),
//                    operator = { debounce(200) },
//                    label = " debounce(200) ",
//                )
//                FlowCase(
//                    inputFlow = generateEventFlow(),
//                    operator = { sample(200) },
//                    label = " sample(200) ",
//                )
                FlowCase(
                    inputFlow = generateEventFlow(color = EventColor.RED),
                    operator = { transform {
                        emit(it)
                        emit(it.copy(time = mutableStateOf(it.time.value + 100), shape = EventShape.Diamond))
                    } },
                    label = """
                        transform {
                            emit(it)
                            delay(100)
                            emit(it.copy(shape = Diamond))
                        }
                    """.trimIndent(),
                )
                FlowCase(
                    inputFlow =
                        generateEventFlow(3, EventColor.PURPLE, EventShape.Diamond) +
                                generateEventFlow(3, EventColor.YELLOW, EventShape.Circle),
                    operator = { distinctUntilChanged { old, new -> old.shape == new.shape } },
                    label = " distinctUntilChanged { old, new -> old.shape == new.shape } ",
                )
                FlowMerge(
                    inputFlow1 = generateEventFlow(4, color = EventColor.GREEN, shape = EventShape.Diamond),
                    inputFlow2 = generateEventFlow(4, color = EventColor.RED, shape = EventShape.Circle),
                )
            }
        }
    }
}
