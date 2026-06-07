package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.terrakok.flowmarbles.EventShape.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FlowFilter(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.filter { it.shape == CIRCLE }
        },
        text = "filter { it.shape == CIRCLE }",
        modifier = modifier
    )
}

@Composable
fun FlowDrop(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.drop(3)
        },
        text = "drop(3)",
        modifier = modifier
    )
}

@Composable
fun FlowDropWhile(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.dropWhile { it.value < 3 }
        },
        text = "dropWhile { it.value < 3 }",
        modifier = modifier
    )
}

@Composable
fun FlowTake(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.take(5)
        },
        text = "take(5)",
        modifier = modifier
    )
}

@Composable
fun FlowTakeWhile(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.takeWhile { it.value < 5 }
        },
        text = "takeWhile { it.value < 5 }",
        modifier = modifier
    )
}

@OptIn(FlowPreview::class)
@Composable
fun FlowDebounce(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.debounce(200.milliseconds)
        },
        text = "debounce(200.milliseconds)",
        modifier = modifier
    )
}

@OptIn(FlowPreview::class)
@Composable
fun FlowSample(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                350L to CIRCLE,
                430L to SQUARE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.sample(200.milliseconds)
        },
        text = "sample(200.milliseconds)",
        modifier = modifier
    )
}

@Composable
fun FlowDistinctUntilChangedBy(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to SQUARE,
                430L to SQUARE,
                500L to CIRCLE,
                700L to RHOMBUS,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.distinctUntilChangedBy { it.shape }
        },
        text = "distinctUntilChangedBy { it.shape }",
        modifier = modifier
    )
}