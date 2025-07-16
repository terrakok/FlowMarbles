package com.github.terrakok.flowmarbles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.flow.withIndex
import kotlin.random.Random


@Composable
fun FlowMap(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents() },
        operator = { f1 ->
            f1.map { it.copy(color = Event.YELLOW) }
        },
        text = "map { it.copy(color = YELLOW) }",
        modifier = modifier
    )
}

@Composable
fun FlowMapLatest(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents() },
        operator = { f1 ->
            f1.mapLatest {
                delay(200)
                it.copy(time = it.time + 200, color = Event.YELLOW)
            }
        },
        text = """
            mapLatest {
                delay(200)
                it.copy(color = YELLOW) 
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowTransform(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents() },
        operator = { f1 ->
            f1.transform {
                emit(it.copy(color = Event.YELLOW))
                delay(100)
                emit(it.copy(time = it.time + 100, color = Event.GREEN))
            }
        },
        text = """
            transform { 
                emit(it.copy(color = YELLOW))
                delay(100)
                emit(it.copy(color = BROWN))
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowTransformLatest(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents() },
        operator = { f1 ->
            f1.transformLatest {
                emit(it.copy(color = Event.YELLOW))
                delay(100)
                emit(it.copy(time = it.time + 100, color = Event.GREEN))
            }
        },
        text = """
            transformLatest { 
                emit(it.copy(color = YELLOW))
                delay(100)
                emit(it.copy(color = BROWN))
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowTransformWhile(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents() },
        operator = { f1 ->
            f1.transformWhile {
                delay(100)
                emit(it.copy(time = it.time + 100, color = Event.PURPLE))
                it.value < 2
            }
        },
        text = """
            transformWhile { 
                delay(100)
                emit(it.copy(color = PURPLE))
                it.value < 2
            }
        """.trimIndent(),
        modifier = modifier
    )
}

@Composable
fun FlowWithIndex(modifier: Modifier = Modifier) {
    FlowCase1(
        input1 = remember { generateMutableEvents(value = { Random.nextInt(3, 30) }) },
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
        input1 = remember { generateMutableEvents() },
        operator = { f1 ->
            f1.runningReduce { acc, value ->
                acc.copy(time = value.time, value = acc.value + value.value)
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