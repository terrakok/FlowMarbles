package com.github.terrakok.flowmarbles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.terrakok.flowmarbles.Event.Data
import com.github.terrakok.flowmarbles.Event.RED
import com.github.terrakok.flowmarbles.Event.Shape
import kotlin.random.Random

data class MutableEvent(val data: Event.Data) {
    val timeState = mutableStateOf(data.time)
}


fun generateMutableEvents(
    count: Int = 5,
    colors: List<Color> = listOf(RED),
    shapes: List<Shape> = listOf(Shape.Circle),
    value: (Int) -> Int = { it }
): List<MutableEvent> = (1..count)
    .map { Random.nextLong(0, MAX_TIME) }
    .sorted()
    .mapIndexed { i, v ->
        MutableEvent(
            Data(v, value(i), colors[i % colors.size], shapes[i % shapes.size])
        )
    }

@Composable
fun EventFlowView(
    events: List<MutableEvent>,
    draggable: Boolean = false,
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
        events.forEachIndexed { index, event ->
            var eventTime by event.timeState
            val xPos = arrowWidthPx + (availableWidth * (eventTime.toFloat() / MAX_TIME))

            Box(
                modifier = Modifier
                    .offset(
                        x = with(LocalDensity.current) { xPos.toDp() - eventSize / 2 },
                        y = maxHeight / 2 - eventSize / 2
                    )
                    .size(eventSize)
                    .draggable(
                        enabled = draggable,
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            val minOffset = -xPos + arrowWidthPx
                            val maxOffset = availableWidth - xPos
                            val offsetX = delta.coerceIn(minOffset, maxOffset)
                            eventTime = ((xPos + offsetX - arrowWidthPx) * MAX_TIME / availableWidth).toLong()
                        }
                    )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = event.data.color,
                    shape = when (event.data.shape) {
                        Event.Shape.Square -> RoundedCornerShape(0)
                        Event.Shape.Circle -> RoundedCornerShape(50)
                        Event.Shape.Diamond -> CutCornerShape(50)
                    },
                    shadowElevation = 4.dp,
                ) {
                    Text(
                        text = event.data.value.toString(),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center).wrapContentSize(),
                        style = MaterialTheme.typography.labelSmall.copy(),
                    )
                }
            }
        }
    }
}
