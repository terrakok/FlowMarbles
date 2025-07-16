package com.github.terrakok.flowmarbles

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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

                val filterItems = remember {
                    listOf(
                        Operator("Filter", mutableStateOf(true)) { FlowFilter() },
                        Operator("Drop") { FlowDrop() },
                        Operator("DropWhile") { FlowDropWhile() },
                        Operator("Take") { FlowTake() },
                        Operator("TakeWhile") { FlowTakeWhile() },
                        Operator("Debounce") { FlowDebounce() },
                        Operator("Sample") { FlowSample() },
                        Operator("DistinctUntilChanged") { FlowDistinctUntilChangedBy() },
                    )
                }

                val transformationItems = remember {
                    listOf(
                        Operator("Map") { FlowMap() },
                        Operator("MapLatest") { FlowMapLatest() },
                        Operator("Transform") { FlowTransform() },
                        Operator("TransformLatest") { FlowTransformLatest() },
                        Operator("TransformWhile") { FlowTransformWhile() },
                        Operator("WithIndex") { FlowWithIndex() },
                        Operator("RunningReduce") { FlowRunningReduce() },
                    )
                }

                val combinationItems = remember {
                    listOf(
                        Operator("Merge", mutableStateOf(true)) { FlowMerge() },
                        Operator("Combine") { FlowCombine() },
                        Operator("Zip") { FlowZip() },
                    )
                }

                Column {
                    Text(text = "Filter Operators", style = MaterialTheme.typography.labelLarge)
                    filterItems.forEach { item -> OperatorChip(item) }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Transformation Operators", style = MaterialTheme.typography.labelLarge)
                    transformationItems.forEach { item -> OperatorChip(item) }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Combination Operators", style = MaterialTheme.typography.labelLarge)
                    combinationItems.forEach { item -> OperatorChip(item) }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    (filterItems + transformationItems + combinationItems).forEach { item ->
                        if (item.enabled.value) { item.view() }
                    }
                }
            }
        }
    }
}

data class Operator(
    val name: String,
    val enabled: MutableState<Boolean> = mutableStateOf(false),
    val view: @Composable () -> Unit
)

@Composable
fun OperatorChip(operator: Operator) {
    var enabled by operator.enabled
    FilterChip(
        onClick = { enabled = !enabled },
        selected = enabled,
        label = { Text(operator.name) },
        elevation = null
    )
}