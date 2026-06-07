package com.github.terrakok.flowmarbles

import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.time.Duration.Companion.milliseconds

enum class EventShape {
    CIRCLE,
    SQUARE,
    RHOMBUS
}

data class Event(
    val time: Long,
    val value: Int,
    val shape: EventShape
)

fun List<Event>.asFlow() = flow {
    var prevTime = 0L
    this@asFlow.forEach { eventData ->
        delay((eventData.time - prevTime).milliseconds)
        prevTime = eventData.time
        emit(eventData)
    }
}.buffer(2) //buffer is here because we want to show a moment of the production instead of the consumption.
//the problem is noticable with ZIP operator, for example

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Flow<Event>.asList(): List<Event> = coroutineScope {
    val scheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(scheduler)
    var result: List<Event>? = null
    launch(testDispatcher) {
        result = this@asList
            .map { it.copy(time = scheduler.currentTime) }
            .toList()
    }
    scheduler.advanceUntilIdle()
    result!!
}