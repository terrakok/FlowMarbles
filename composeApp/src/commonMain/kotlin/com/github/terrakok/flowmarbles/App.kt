package com.github.terrakok.flowmarbles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

enum class EventShape {
    Square, Circle, Diamond
}

data class EventData(
    val time: Int,
    val value: Int,
    val color: Color,
    val shape: EventShape
)

typealias EventFlow = List<EventData>

private const val MAX_TIME = 1000
private val eventColors = listOf(
    Color(0xFF6750A4),
    Color(0xFF7F67BE),
    Color(0xFFB4B0DC),
    Color(0xFF4A6FA5),
    Color(0xFF47B881),
    Color(0xFFFFB4AB),
    Color(0xFFE46962),
    Color(0xFFFFB94E),
    Color(0xFF00A2B8),
    Color(0xFFF85977)

)

private fun generateEventFlow(
    count: Int,
    color: Color = eventColors[Random.nextInt(eventColors.size)],
    shape: EventShape = EventShape.entries[Random.nextInt(EventShape.entries.size)]
): EventFlow = (1..count)
    .map { Random.nextInt(0, MAX_TIME) }
    .sorted()
    .mapIndexed { i, v ->
        EventData(
            v,
            i,
            color,
            shape
        )
    }

@Composable
fun EventFlowView(
    flow: EventFlow,
    onChangeEvent: (index: Int, new: EventData) -> Unit,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.onSurface
    val eventSize = 40.dp

    BoxWithConstraints(
        modifier = modifier
    ) {
        val arrowWidth = 12.dp
        val arrowWidthPx = with(LocalDensity.current) { arrowWidth.toPx() }
        
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawLine(
                color = lineColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = lineColor,
                start = Offset(size.width, size.height / 2),
                end = Offset(size.width - arrowWidthPx, (size.height - arrowWidthPx) / 2),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = lineColor,
                start = Offset(size.width, size.height / 2),
                end = Offset(size.width - arrowWidthPx, (size.height + arrowWidthPx) / 2),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = lineColor,
                start = Offset(arrowWidthPx, (size.height - arrowWidthPx) / 2),
                end = Offset(arrowWidthPx, (size.height + arrowWidthPx) / 2),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }

        val availableWidth = with(LocalDensity.current) {
            (maxWidth - arrowWidth * 2).toPx()
        }
        // Draw circles for each event
        flow.forEachIndexed { index, event ->
            val xPos =
                arrowWidthPx + (availableWidth * (event.time.toFloat() / MAX_TIME))

            Box(
                modifier = Modifier
                    .offset(
                        x = with(LocalDensity.current) { xPos.toDp() - eventSize / 2 },
                        y = maxHeight / 2 - eventSize / 2
                    )
                    .size(eventSize)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = event.color,
                    shape = when(event.shape) {
                        EventShape.Square -> RoundedCornerShape(0)
                        EventShape.Circle -> RoundedCornerShape(50)
                        EventShape.Diamond -> CutCornerShape(50)
                    },
                    shadowElevation = 4.dp,
                ) {
                    Text(
                        text = event.value.toString(),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center).wrapContentSize(),
                        style = MaterialTheme.typography.labelSmall.copy(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun App() = AppTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val f = remember { generateEventFlow(10) }
        EventFlowView(
            flow = f,
            onChangeEvent = { _, _ -> },
            modifier = Modifier.fillMaxWidth().height(400.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        )
    }
}
