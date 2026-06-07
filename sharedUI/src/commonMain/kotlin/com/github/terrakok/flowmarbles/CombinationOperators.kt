package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.terrakok.flowmarbles.EventShape.CIRCLE
import com.github.terrakok.flowmarbles.EventShape.RHOMBUS
import com.github.terrakok.flowmarbles.EventShape.SQUARE
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        input1 = remember {
            makeFlow(30L to CIRCLE, 200L to CIRCLE, 600L to CIRCLE)
        },
        input2 = remember {
            makeFlow(100L to SQUARE, 400L to SQUARE, 700L to SQUARE)
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
        input1 = remember {
            makeFlow(30L to CIRCLE, 100L to CIRCLE, 600L to CIRCLE)
        },
        input2 = remember {
            makeFlow(300L to RHOMBUS, 400L to SQUARE, 700L to CIRCLE)
        },
        operator = { f1, f2 ->
            f1.combine(f2) { first, second ->
                Event(
                    time = 0,
                    value = first.value + second.value,
                    shape = first.shape,
                )
            }
        },
        text = """
            flowA.combine(flowB) { first, second -> Event(
                value = first.value + second.value,
                shape = first.shape
            ) }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowZip(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember {
            makeFlow(30L to CIRCLE, 200L to CIRCLE, 300L to CIRCLE)
        },
        input2 = remember {
            makeFlow(70L to SQUARE, 500L to SQUARE, 700L to SQUARE)
        },
        operator = { f1, f2 ->
            f1.zip(f2) { first, second ->
                Event(
                    time = 0,
                    value = first.value + second.value,
                    shape = first.shape
                )
            }
        },
        text = """
            flowA.zip(flowB) { first, second -> Event(
                value = first.value + second.value,
                shape = first.shape
            ) }
        """.trimIndent(),
        modifier = modifier
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FlowFlatMapMerge(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember {
            makeFlow(130L to CIRCLE, 200L to CIRCLE, 700L to CIRCLE)
        },
        input2 = remember {
            makeFlow(20L to SQUARE, 200L to SQUARE)
        },
        operator = { f1, f2 ->
            f1.flatMapMerge { f2 }
        },
        text = "flatMapMerge { f2 }",
        modifier = modifier
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FlowFlatMapConcat(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember {
            makeFlow(30L to CIRCLE, 150L to CIRCLE, 700L to CIRCLE)
        },
        input2 = remember {
            makeFlow(50L to SQUARE, 200L to SQUARE)
        },
        operator = { f1, f2 ->
            f1.flatMapConcat { f2 }
        },
        text = "flatMapConcat { f2 }",
        modifier = modifier
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FlowFlatMapLatest(modifier: Modifier = Modifier) {
    FlowCase2(
        input1 = remember {
            makeFlow(30L to CIRCLE, 200L to CIRCLE, 700L to CIRCLE)
        },
        input2 = remember {
            makeFlow(20L to SQUARE, 200L to SQUARE)
        },
        operator = { f1, f2 ->
            f1.flatMapLatest { f2 }
        },
        text = "flatMapLatest { f2 }",
        modifier = modifier
    )
}