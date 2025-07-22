package com.github.terrakok.flowmarbles

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

const val MAX_TIME = 1000L

object Event {
    val PURPLE = Color(0xFF9c27b0)
    val BLUE = Color(0xFF5D6DE1)
    val YELLOW = Color(0xFFff9800)
    val RED = Color(0xFFf44336)
    val GREEN = Color(0xFF4caf50)

    data class Data(
        val time: Long,
        val value: Int,
        val color: Color
    )
}

fun List<Event.Data>.asFlow() = flow {
    var prevTime = 0L
    this@asFlow.forEach { eventData ->
        delay(eventData.time - prevTime)
        prevTime = eventData.time
        emit(eventData)
    }
}.buffer(2) //buffer is here because we want to show a moment of the production instead of the consumption.
//the problem is noticable with ZIP operator, for example

suspend fun Flow<Event.Data>.asList(): List<Event.Data> = coroutineScope {
    val scheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(scheduler)
    var result: List<Event.Data>? = null
    launch(testDispatcher) {
        result = this@asList
            .map { it.copy(time = scheduler.currentTime) }
            .toList()
    }
    scheduler.advanceUntilIdle()
    result!!
}
