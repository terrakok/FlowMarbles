package com.github.terrakok.flowmarbles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.terrakok.flowmarbles.theme.Icons

sealed class ThemeMode {
    data object Light : ThemeMode()
    data object System : ThemeMode()
    data object Dark : ThemeMode()

    fun toBoolean(): Boolean? = when (this) {
        Light -> false
        Dark -> true
        System -> null
    }

    companion object {
        fun fromBoolean(isDark: Boolean?): ThemeMode = when (isDark) {
            true -> Dark
            false -> Light
            null -> System
        }
    }
}

@Composable
fun ThemeSwitcher(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(ThemeMode.Light, ThemeMode.System, ThemeMode.Dark)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 4.dp),
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxSize(),
        ) {
            options.forEachIndexed { index, mode ->
                val isSelected = mode == selectedMode

                SegmentedButton(
                    selected = isSelected,
                    onClick = { onModeSelected(mode) },
                    shape = when (index) {
                        0 -> RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                        options.size - 1 -> RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                        else -> RoundedCornerShape(0.dp)
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        inactiveContainerColor = Color.Transparent,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    icon = {
                        ThemeSwitcherIcon(
                            icon = mode.icon,
                            isSelected = isSelected,
                        )
                    },
                ) {
                    Text(
                        text = mode.label,
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeSwitcherIcon(
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = rememberVectorPainter(icon),
        contentDescription = null,
        modifier = modifier.padding(horizontal = 2.dp),
        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private val ThemeMode.icon: ImageVector
    @Composable get() = when (this) {
        ThemeMode.Light -> Icons.Sun
        ThemeMode.System -> Icons.AutoTheme
        ThemeMode.Dark -> Icons.Moon
    }

private val ThemeMode.label: String
    @Composable get() = when (this) {
        ThemeMode.Light -> "light"
        ThemeMode.System -> "auto"
        ThemeMode.Dark -> "dark"
    }
