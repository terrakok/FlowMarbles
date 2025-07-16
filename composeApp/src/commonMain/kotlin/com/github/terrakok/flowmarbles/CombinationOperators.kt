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
import kotlinx.coroutines.flow.combine
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
                colors = listOf(Event.GREEN),
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
                colors = listOf(Event.GREEN, Event.YELLOW, Event.BLUE),
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
                colors = listOf(Event.GREEN),
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