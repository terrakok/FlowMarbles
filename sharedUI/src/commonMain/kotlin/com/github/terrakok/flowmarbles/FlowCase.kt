package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.theme.Icons
import kotlinx.coroutines.flow.Flow

@Composable
fun FlowCaseCard(
    vararg inputs: List<MutableEvent>,
    text: String,
    result: List<MutableEvent>,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Box {
            Icon(
                imageVector = Icons.Gesture,
                contentDescription = "Gesture",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            )

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
                SelectionContainer(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium.copy()
                    )
                }
                EventFlowView(
                    events = result,
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                )
            }
        }
    }
}

@Composable
fun FlowCase1(
    input1: List<MutableEvent>,
    operator: (Flow<Event>) -> Flow<Event>,
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
    operator: (Flow<Event>, Flow<Event>) -> Flow<Event>,
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