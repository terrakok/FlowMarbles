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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun EventFlowView(
    flow: EventFlow,
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
        flow.forEachIndexed { index, event ->
            var eventTime by event.time
            val xPos = arrowWidthPx + (availableWidth * (eventTime.toFloat() / MAX_TIME))

            var offsetX by remember { mutableStateOf(0f) }
            Box(
                modifier = Modifier
                    .offset(
                        x = with(LocalDensity.current) { (xPos + offsetX).toDp() - eventSize / 2 },
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
                            eventTime = ((xPos + offsetX - arrowWidthPx) * MAX_TIME / availableWidth).toInt()
                        }
                    )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = event.color.color,
                    shape = when (event.shape) {
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
