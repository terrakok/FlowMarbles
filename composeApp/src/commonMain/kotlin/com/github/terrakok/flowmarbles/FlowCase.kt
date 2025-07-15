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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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