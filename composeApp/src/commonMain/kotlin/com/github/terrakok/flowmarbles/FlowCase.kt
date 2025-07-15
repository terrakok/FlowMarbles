package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher

@Composable
fun FlowCase(
    inputFlow: EventFlow,
    operator: Flow<EventData>.() -> Flow<EventData>,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            EventFlowView(
                flow = inputFlow,
                draggable = true,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )
            Text(
                text = label,
                modifier = Modifier.wrapContentHeight().padding(16.dp).align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleMedium.copy()
            )
            val result = inputFlow.flowOperator(operator)
            EventFlowView(
                flow = result,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )
        }
    }
}

private fun List<EventData>.toEventFlow() = flow {
    this@toEventFlow.sortedBy { it.time.value }.forEach { eventData ->
        delay(eventData.time.value.toLong())
        emit(eventData)
    }
}

@Composable
private fun List<EventData>.flowOperator(
    operator: Flow<EventData>.() -> Flow<EventData>
): List<EventData> {
    var result by remember { mutableStateOf(emptyList<EventData>()) }
    val k = this.joinToString("") { it.time.value.toString() }
    LaunchedEffect(k) {
        val testDispatcher = StandardTestDispatcher()
        launch(testDispatcher) {
            result = this@flowOperator.toEventFlow().operator().toList()
        }
        testDispatcher.scheduler.advanceUntilIdle()
    }
    return result
}

@Composable
fun FlowMerge(
    inputFlow1: EventFlow,
    inputFlow2: EventFlow,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            EventFlowView(
                flow = inputFlow1,
                draggable = true,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )
            EventFlowView(
                flow = inputFlow2,
                draggable = true,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )
            Text(
                text = "merge(flowA, flowB)",
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy()
            )
            val result = inputFlow1.flowOperator { merge(this, inputFlow2.toEventFlow()) }
            EventFlowView(
                flow = result,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )
        }
    }
}