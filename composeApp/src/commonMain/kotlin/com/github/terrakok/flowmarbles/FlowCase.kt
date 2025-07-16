package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow

@Composable
fun FlowCaseCard(
    vararg inputs: List<MutableEvent>,
    text: String,
    result: List<MutableEvent>,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            inputs.forEach { inputFlow ->
                EventFlowView(
                    events = inputFlow,
                    draggable = true,
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                )
            }
            Text(
                text = text,
                modifier = Modifier.wrapContentHeight().align(Alignment.CenterHorizontally).padding(16.dp),
                style = MaterialTheme.typography.titleMedium.copy()
            )
            EventFlowView(
                events = result,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )
        }
    }
}

@Composable
fun FlowCase1(
    input1: List<MutableEvent>,
    operator: (Flow<Event.Data>) -> Flow<Event.Data>,
    text: String,
    modifier: Modifier = Modifier
) {
    val f1 by remember {
        derivedStateOf { input1.map { it.data.copy(time = it.timeState.value) }.sortedBy { it.time } }
    }

    var result by remember { mutableStateOf(emptyList<MutableEvent>()) }
    LaunchedEffect(f1) {
        result = operator(f1.asFlow()).asList().map { MutableEvent(it) }
    }

    FlowCaseCard(
        input1,
        text = text,
        result = result,
        modifier = modifier
    )
}


@Composable
fun FlowCase2(
    input1: List<MutableEvent>,
    input2: List<MutableEvent>,
    operator: (Flow<Event.Data>, Flow<Event.Data>) -> Flow<Event.Data>,
    text: String,
    modifier: Modifier = Modifier
) {
    val f1 by remember {
        derivedStateOf { input1.map { it.data.copy(time = it.timeState.value) }.sortedBy { it.time } }
    }
    val f2 by remember {
        derivedStateOf { input2.map { it.data.copy(time = it.timeState.value) }.sortedBy { it.time } }
    }

    var result by remember { mutableStateOf(emptyList<MutableEvent>()) }
    LaunchedEffect(f1, f2) {
        result = operator(f1.asFlow(), f2.asFlow()).asList().map { MutableEvent(it) }
    }

    FlowCaseCard(
        input1, input2,
        text = text,
        result = result,
        modifier = modifier
    )
}