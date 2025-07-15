package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.take

@Composable
fun FlowFilter(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            generateMutableEvents(7, colors = listOf(Event.RED, Event.GREEN))
        },
        operator = { f1 ->
            f1.filter { it.color != Event.GREEN }
        },
        text = "filter { it.color != Event.GREEN }",
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
fun FlowCase1(
    input1: List<MutableEvent>,
    operator: (Flow<Event.Data>) -> Flow<Event.Data>,
    text: String,
    modifier: Modifier = Modifier
) {
    val f1 by remember {
        derivedStateOf { input1.map { it.data.copy(time = it.timeState.value) }.sortedBy { it.time } }
    }

    var result by remember { mutableStateOf(emptyList<MutableEvent>()) }
    LaunchedEffect(f1) {
        result = operator(f1.asFlow()).asList().map { MutableEvent(it) }
    }

    FlowCaseCard(
        input1,
        text = text,
        result = result,
        modifier = modifier
    )
}