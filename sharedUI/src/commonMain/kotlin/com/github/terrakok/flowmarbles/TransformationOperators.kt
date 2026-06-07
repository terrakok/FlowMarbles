package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.terrakok.flowmarbles.EventShape.CIRCLE
import com.github.terrakok.flowmarbles.EventShape.RHOMBUS
import com.github.terrakok.flowmarbles.EventShape.SQUARE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.flow.withIndex
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun FlowMap(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to CIRCLE,
                350L to CIRCLE,
                430L to CIRCLE,
                700L to CIRCLE,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.map { it.copy(shape = RHOMBUS) }
        },
        text = "map { it.copy(shape = RHOMBUS) }",
        modifier = modifier
    )
}

@Composable
fun FlowMapLatest(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                100L to CIRCLE,
                150L to CIRCLE,
                400L to CIRCLE,
                530L to CIRCLE,
                600L to CIRCLE,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.mapLatest {
                delay(200.milliseconds)
                it.copy(shape = RHOMBUS)
            }
        },
        text = """
            mapLatest {
                delay(200.milliseconds)
                it.copy(shape = RHOMBUS)
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowTransform(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                100L to CIRCLE,
                500L to CIRCLE,
                710L to CIRCLE,
                800L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.transform {
                emit(it.copy(shape = RHOMBUS))
                delay(100.milliseconds)
                emit(it.copy(shape = SQUARE))
            }
        },
        text = """
            transform { 
                emit(it.copy(shape = RHOMBUS))
                delay(100.milliseconds)
                emit(it.copy(shape = SQUARE))
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FlowTransformLatest(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                100L to CIRCLE,
                500L to CIRCLE,
                710L to CIRCLE,
                800L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.transformLatest {
                emit(it.copy(shape = RHOMBUS))
                delay(100.milliseconds)
                emit(it.copy(shape = SQUARE))
            }
        },
        text = """
            transformLatest {
                emit(it.copy(shape = RHOMBUS))
                delay(100.milliseconds)
                emit(it.copy(shape = SQUARE))
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowTransformWhile(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                Event(40L, 1, CIRCLE),
                Event(120L, 1, CIRCLE),
                Event(270L, 1, CIRCLE),
                Event(350L, 7, CIRCLE),
                Event(430L, 6, CIRCLE),
                Event(700L, 4, CIRCLE),
                Event(900L, 5, CIRCLE),
            )
        },
        operator = { f1 ->
            f1.transformWhile {
                delay(100.milliseconds)
                emit(it.copy(shape = SQUARE))
                it.value < 2
            }
        },
        text = """
            transformWhile {
                delay(100.milliseconds)
                emit(it.copy(shape = SQUARE))
                it.value < 2
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowWithIndex(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                Event(40L, 7, CIRCLE),
                Event(120L, 6, CIRCLE),
                Event(270L, 3, CIRCLE),
                Event(350L, 1, CIRCLE),
                Event(430L, 1, CIRCLE),
                Event(700L, 4, CIRCLE),
                Event(900L, 7, CIRCLE),
            )
        },
        operator = { f1 ->
            f1.withIndex().map { (index, value) -> value.copy(value = index) }
        },
        text = "withIndex().map { (index, value) -> value.copy(value = index) }",
        modifier = modifier
    )
}

@Composable
fun FlowRunningReduce(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember {
            makeFlow(
                40L to CIRCLE,
                120L to CIRCLE,
                270L to CIRCLE,
                350L to CIRCLE,
                430L to CIRCLE,
                700L to CIRCLE,
                900L to CIRCLE,
            )
        },
        operator = { f1 ->
            f1.runningReduce { acc, value ->
                acc.copy(value = acc.value + value.value)
            }
        },
        text = """
            runningReduce { acc, value ->
                acc.copy(value = acc.value + value.value)
            }
        """.trimIndent(),
        modifier = modifier
    )
}