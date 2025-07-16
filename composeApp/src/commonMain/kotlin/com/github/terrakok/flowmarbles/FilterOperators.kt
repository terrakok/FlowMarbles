package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile

@Composable
fun FlowFilter(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            generateMutableEvents(7, colors = listOf(Event.RED, Event.GREEN))
        },
        operator = { f1 ->
            f1.filter { it.color != Event.GREEN }
        },
        text = "filter { it.color != GREEN }",
        modifier = modifier
    )
}

@Composable
fun FlowDrop(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents(7) },
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
        input1 = remember { generateMutableEvents(7) },
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
        input1 = remember { generateMutableEvents(7) },
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
        input1 = remember { generateMutableEvents(7) },
        operator = { f1 ->
            f1.takeWhile { it.value < 5 }
        },
        text = "takeWhile { it.value < 5 }",
        modifier = modifier
    )
}

@Composable
fun FlowDebounce(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents(7) },
        operator = { f1 ->
            f1.debounce(200)
        },
        text = "debounce(200)",
        modifier = modifier
    )
}

@Composable
fun FlowSample(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents(7) },
        operator = { f1 ->
            f1.sample(200)
        },
        text = "sample(200)",
        modifier = modifier
    )
}

@Composable
fun FlowDistinctUntilChangedBy(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            generateMutableEvents(7, colors = listOf(Event.RED, Event.GREEN, Event.GREEN, Event.GREEN))
        },
        operator = { f1 ->
            f1.distinctUntilChangedBy { it.color }
        },
        text = "distinctUntilChangedBy { it.color }",
        modifier = modifier
    )
}