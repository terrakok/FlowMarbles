package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.zip
import kotlin.math.max

@Composable
fun FlowMerge(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember { generateMutableEvents(3) },
        input2 = remember {
            generateMutableEvents(
                count = 3,
                colors = listOf(Event.BROWN),
                shapes = listOf(Event.Shape.Diamond)
            )
        },
        operator = { f1, f2 ->
            merge(f1, f2)
        },
        text = "merge(flowA, flowB)",
        modifier = modifier
    )
}

@Composable
fun FlowCombine(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember { generateMutableEvents(3) },
        input2 = remember {
            generateMutableEvents(
                count = 3,
                colors = listOf(Event.BROWN, Event.YELLOW, Event.BLUE),
                shapes = listOf(Event.Shape.Diamond)
            )
        },
        operator = { f1, f2 ->
            f1.combine(f2) { first, second ->
                Event.Data(
                    time = max(first.time, second.time),
                    value = first.value + second.value,
                    color = first.color,
                    shape = second.shape,
                )
            }
        },
        text = """
            flowA.combine(flowB) { first, second -> Event(
                value = first.value + second.value,
                color = first.color, 
                shape = second.shape 
            ) }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowZip(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember { generateMutableEvents(3) },
        input2 = remember {
            generateMutableEvents(
                count = 3,
                colors = listOf(Event.BROWN),
                shapes = listOf(Event.Shape.Diamond)
            )
        },
        operator = { f1, f2 ->
            f1.zip(f2) { first, second ->
                Event.Data(
                    time = max(first.time, second.time),
                    value = first.value + second.value,
                    color = first.color,
                    shape = second.shape,
                )
            }
        },
        text = """
            flowA.zip(flowB) { first, second -> Event(
                value = first.value + second.value,
                color = first.color, 
                shape = second.shape 
            ) }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowFlatMapMerge(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember { generateMutableEvents(3) },
        input2 = remember {
            listOf(
                MutableEvent(Event.Data(10, 1, Event.PURPLE, Event.Shape.Diamond)),
                MutableEvent(Event.Data(70, 2, Event.PURPLE, Event.Shape.Diamond)),
            )
        },
        operator = { f1, f2 ->
            f1.flatMapMerge { event -> f2.map { it.copy(time = event.time + it.time) } }
        },
        text = "flatMapMerge { f2 }",
        modifier = modifier
    )
}

@Composable
fun FlowFlatMapConcat(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember { generateMutableEvents(3) },
        input2 = remember {
            listOf(
                MutableEvent(Event.Data(10, 1, Event.PURPLE, Event.Shape.Diamond)),
                MutableEvent(Event.Data(70, 2, Event.PURPLE, Event.Shape.Diamond)),
            )
        },
        operator = { f1, f2 ->
            f1.flatMapConcat { event -> f2.map { it.copy(time = event.time + it.time) } }
        },
        text = "flatMapConcat { f2 }",
        modifier = modifier
    )
}

@Composable
fun FlowFlatMapLatest(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember { generateMutableEvents(3) },
        input2 = remember {
            listOf(
                MutableEvent(Event.Data(10, 1, Event.PURPLE, Event.Shape.Diamond)),
                MutableEvent(Event.Data(70, 2, Event.PURPLE, Event.Shape.Diamond)),
            )
        },
        operator = { f1, f2 ->
            f1.flatMapLatest { event -> f2.map { it.copy(time = event.time + it.time) } }
        },
        text = "flatMapLatest { f2 }",
        modifier = modifier
    )
}