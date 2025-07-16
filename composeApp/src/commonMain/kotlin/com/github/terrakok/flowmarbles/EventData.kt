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

const val MAX_TIME = 1000L

object Event {
    enum class Shape { Square, Circle, Diamond }

    val PURPLE = Color(0xFF9c27b0)
    val BLUE = Color(0xFF3f51b5)
    val YELLOW = Color(0xFFff9800)
    val GREY = Color(0xFF607d8b)
    val BROWN = Color(0xFF795548)

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
