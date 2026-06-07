package com.github.terrakok.flowmarbles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

val allOperators = mapOf(
    "Filter" to listOf(
        Operator("Filter", FilterDoc, { FlowFilter(it) }),
        Operator("Drop", DropDoc, { FlowDrop(it) }),
        Operator("DropWhile", DropWhileDoc, { FlowDropWhile(it) }),
        Operator("Take", TakeDoc, { FlowTake(it) }),
        Operator("TakeWhile", TakeWhileDoc, { FlowTakeWhile(it) }),
        Operator("Debounce", DebounceDoc, { FlowDebounce(it) }),
        Operator("Sample", SampleDoc, { FlowSample(it) }),
        Operator("DistinctUntilChanged", DistinctUntilChangedDoc, { FlowDistinctUntilChangedBy(it) }),
    ),
    "Transformation" to listOf(
        Operator("Map", MapDoc, { FlowMap(it) }),
        Operator("MapLatest", MapLatestDoc, { FlowMapLatest(it) }),
        Operator("Transform", TransformDoc, { FlowTransform(it) }),
        Operator("TransformLatest", TransformLatestDoc, { FlowTransformLatest(it) }),
        Operator("TransformWhile", TransformWhileDoc, { FlowTransformWhile(it) }),
        Operator("WithIndex", WithIndexDoc, { FlowWithIndex(it) }),
        Operator("RunningReduce", RunningReduceDoc, { FlowRunningReduce(it) }),
    ),
    "Combination" to listOf(
        Operator("Merge", MergeDoc, { FlowMerge(it) }),
        Operator("Combine", CombineDoc, { FlowCombine(it) }),
        Operator("Zip", ZipDoc, { FlowZip(it) }),
        Operator("FlatMapMerge", FlatMapMergeDoc, { FlowFlatMapMerge(it) }),
        Operator("FlatMapConcat", FlatMapConcatDoc, { FlowFlatMapConcat(it) }),
        Operator("FlatMapLatest", FlatMapLatestDoc, { FlowFlatMapLatest(it) }),
    ),
)

data class Operator(
    val name: String,
    val doc: String,
    val content: @Composable (Modifier) -> Unit
)

@Composable
fun OperatorsList(
    selected: Operator,
    onOperatorSelected: (Operator) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        allOperators.entries.forEach { (sectionTitle, operators) ->
            item {
                Text(
                    text = sectionTitle.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing
                    ),
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 8.dp,
                        start = 4.dp
                    )
                )
            }
            items(operators, key = { it.name }) { operator ->
                OperatorItem(
                    operator = operator,
                    isSelected = selected == operator,
                    onClick = { onOperatorSelected(operator) }
                )
            }
        }
    }
}

@Composable
private fun OperatorItem(
    operator: Operator,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bullet point
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(0.5f))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Operator name
        Text(
            text = operator.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(0.5f),
                fontWeight = if (isSelected) FontWeight.W500 else FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
