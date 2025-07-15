package com.github.terrakok.flowmarbles

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

enum class EventShape {
    Square, Circle, Diamond
}

data class EventData(
    val time: MutableState<Int>,
    val value: Int,
    val color: EventColor,
    val shape: EventShape
)

typealias EventFlow = List<EventData>

const val MAX_TIME = 1000

enum class EventColor(val color: Color) {
    PURPLE(Color(0xFF7F67BE)),
    BLUE(Color(0xFF4A6FA5)),
    GREEN(Color(0xFF47B881)),
    RED(Color(0xFFE46962)),
    YELLOW(Color(0xFFFFB94E)),
}

fun generateEventFlow(
    count: Int = 5,
    color: EventColor = EventColor.BLUE,
    shape: EventShape = EventShape.Circle
): EventFlow = (1..count)
    .map { Random.nextInt(0, MAX_TIME) }
    .mapIndexed { i, v ->
        EventData(
            mutableStateOf(v),
            i,
            color,
            shape
        )
    }