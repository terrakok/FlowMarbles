package com.github.terrakok.flowmarbles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Stable
data class MutableEvent(val data: Event) {
    val timeState = mutableStateOf(data.time)
}

private const val MAX_TIME = 1000L

fun makeFlow(vararg events: Pair<Long, EventShape>) =
    events.mapIndexed { index, (time, shape) ->
        MutableEvent(Event(time, index + 1, shape))
    }

fun makeFlow(vararg events: Event) =
    events.map { MutableEvent(it) }

@Composable
fun EventFlowView(
    events: List<MutableEvent>,
    draggable: Boolean = false,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.onSurface
    val eventSize = 30.dp

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
                    color = when (event.data.shape) {
                        EventShape.CIRCLE -> Color(0xFFC53030)
                        EventShape.SQUARE -> Color(0xFF2B6CB0)
                        EventShape.RHOMBUS -> Color(0xFF2F855A)
                    },
                    shape = when (event.data.shape) {
                        EventShape.CIRCLE -> RoundedCornerShape(50)
                        EventShape.SQUARE -> RoundedCornerShape(0)
                        EventShape.RHOMBUS -> CutCornerShape(50)
                    },
                    shadowElevation = 4.dp,
                ) {
                    Text(
                        text = event.data.value.toString(),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center).wrapContentSize(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}