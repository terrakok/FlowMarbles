package com.github.terrakok.flowmarbles

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.random.Random

const val MAX_TIME = 1000L

object Event {
    enum class Shape { Square, Circle, Diamond }

    val PURPLE = Color(0xFF7F67BE)
    val BLUE = Color(0xFF4A6FA5)
    val GREEN = Color(0xFF47B881)
    val RED = Color(0xFFE46962)
    val YELLOW = Color(0xFFFFB94E)

    data class Data(
        val time: Long,
        val value: Int,
        val color: Color,
        val shape: Shape
    )
}

fun List<Event.Data>.asFlow() = flow {
    var prevTime = 0L
    this@asFlow.forEach { eventData ->
        delay(eventData.time - prevTime)
        prevTime = eventData.time
        emit(eventData)
    }
}

suspend fun Flow<Event.Data>.asList(): List<Event.Data> = coroutineScope {
    val scheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(scheduler)
    var result: List<Event.Data>? = null
    launch(testDispatcher) {
        result = this@asList.toList()
    }
    scheduler.advanceUntilIdle()
    result!!
}
