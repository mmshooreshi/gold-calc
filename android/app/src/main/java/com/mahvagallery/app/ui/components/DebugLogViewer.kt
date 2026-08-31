package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.model.LogEntry
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun DebugLogViewer(
    logs: List<LogEntry>,
    onCopyLogs: () -> Unit,
    onClearLogs: () -> Unit,
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Terminal,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "لاگ‌های سیستم و دیباگ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF172051).copy(alpha = 0.08f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${NumberFormatters.toPersianDigits(logs.size.toString())} رکورد",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                }
            }

            // Terminal Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 220.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F172A))
                    .padding(10.dp)
            ) {
                if (logs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        Text(text = "هیچ لاگی ثبت نشده است", color = Color(0xFF64748B), fontSize = 11.5.sp)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(logs) { log ->
                            val levelColor = when (log.level) {
                                "ERROR" -> Color(0xFFEF4444)
                                "WARN" -> Color(0xFFF59E0B)
                                "DEBUG" -> Color(0xFF3B82F6)
                                else -> Color(0xFF10B981)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "[${log.timestamp}]",
                                    color = Color(0xFF64748B),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "[${log.tag}]",
                                    color = levelColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = log.message,
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 10.5.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClearLogs,
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Icon(Icons.Filled.CleaningServices, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextMuted)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "پاک‌سازی", fontSize = 11.5.sp, color = TextDark)
                }

                Button(
                    onClick = onCopyLogs,
                    modifier = Modifier.weight(1f).height(38.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark, contentColor = White)
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "کپی لاگ‌ها", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
