package com.mahvagallery.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.viewmodel.AppTab

@Composable
fun AppBottomBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp)
            .background(colors.surface)
            .navigationBarsPadding()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.values().forEach { tab ->
                val isSelected = tab == currentTab
                val scale by animateFloatAsState(targetValue = if (isSelected) 1.08f else 1f, label = "scale")
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) colors.primary else colors.textMuted,
                    label = "color"
                )

                val icon: ImageVector = when (tab) {
                    AppTab.CALCULATOR -> Icons.Filled.Calculate
                    AppTab.HISTORY -> Icons.Filled.History
                    AppTab.STATS -> Icons.Filled.Insights
                    AppTab.SETTINGS -> Icons.Filled.Settings
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(width = 24.dp, height = 3.dp)
                                .background(colors.primary, RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
                        )
                    } else {
                        Box(modifier = Modifier.size(width = 24.dp, height = 3.dp))
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = tab.title,
                        modifier = Modifier
                            .size(24.dp)
                            .scale(scale),
                        tint = iconColor
                    )
                    Text(
                        text = tab.title,
                        fontSize = scaledSp(11f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = iconColor
                    )
                }
            }
        }
    }
}
