package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp

@Composable
fun TopHeaderBar(
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(
                Brush.linearGradient(
                    colors = if (colors.isDark) {
                        listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                    } else {
                        listOf(Color(0xFF172051), Color(0xFF2A3675))
                    }
                )
            )
            .statusBarsPadding()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "گالری طلا مهوا",
                color = Color.White,
                fontSize = scaledSp(18.5f),
                fontFamily = VazirmatnFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
