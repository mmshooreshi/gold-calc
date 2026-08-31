package com.mahvagallery.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun SimpleBarChart(
    title: String,
    data: List<Pair<String, Double>>,
    color: Color = PrimaryDark,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
        color = White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (data.isEmpty() || data.all { it.second == 0.0 }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "داده‌ای برای نمایش وجود ندارد", fontSize = 12.sp, color = TextMuted)
                }
            } else {
                val maxVal = (data.maxOfOrNull { it.second } ?: 1.0).coerceAtLeast(1.0)

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val width = size.width
                    val height = size.height - 30f
                    val barCount = data.size
                    val slotWidth = width / barCount
                    val barWidth = (slotWidth * 0.5f).coerceAtMost(36f)

                    data.forEachIndexed { i, entry ->
                        val barHeight = ((entry.second / maxVal) * height).toFloat()
                        val x = i * slotWidth + (slotWidth - barWidth) / 2
                        val y = height - barHeight

                        // Draw rounded bar
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(8f, 8f)
                        )
                    }
                }

                // Date labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    data.takeLast(6).forEach { entry ->
                        val shortLabel = if (entry.first.length > 5) entry.first.substring(5) else entry.first
                        Text(
                            text = NumberFormatters.toPersianDigits(shortLabel),
                            fontSize = 9.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleLineChart(
    title: String,
    data: List<Pair<String, Double>>,
    lineColor: Color = Color(0xFF10B981),
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
        color = White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (data.isEmpty() || data.all { it.second == 0.0 }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "داده‌ای برای نمایش وجود ندارد", fontSize = 12.sp, color = TextMuted)
                }
            } else {
                val maxVal = (data.maxOfOrNull { it.second } ?: 1.0).coerceAtLeast(1.0)

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val width = size.width
                    val height = size.height - 20f
                    val count = data.size
                    val stepX = if (count > 1) width / (count - 1) else width

                    val points = data.mapIndexed { i, entry ->
                        val x = if (count > 1) i * stepX else width / 2
                        val y = (height - ((entry.second / maxVal) * height)).toFloat()
                        Offset(x, y)
                    }

                    // Path
                    val path = Path().apply {
                        points.forEachIndexed { idx, pt ->
                            if (idx == 0) moveTo(pt.x, pt.y)
                            else lineTo(pt.x, pt.y)
                        }
                    }

                    // Fill under path
                    val fillPath = Path().apply {
                        addPath(path)
                        lineTo(points.last().x, height)
                        lineTo(points.first().x, height)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(lineColor.copy(alpha = 0.25f), Color.Transparent),
                            startY = 0f,
                            endY = height
                        )
                    )

                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 5f, cap = StrokeCap.Round)
                    )

                    // Draw points
                    points.forEach { pt ->
                        drawCircle(color = White, radius = 7f, center = pt)
                        drawCircle(color = lineColor, radius = 5f, center = pt)
                    }
                }

                // Date labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    data.takeLast(6).forEach { entry ->
                        val shortLabel = if (entry.first.length > 5) entry.first.substring(5) else entry.first
                        Text(
                            text = NumberFormatters.toPersianDigits(shortLabel),
                            fontSize = 9.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
