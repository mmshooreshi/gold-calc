package com.mahvagallery.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.components.HistoryCard
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.viewmodel.HistoryFilter
import com.mahvagallery.app.viewmodel.MainViewModel

@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val historyList by viewModel.filteredHistory.collectAsState()
    val activeFilter by viewModel.historyFilter.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Section Header
        Text(
            text = "تاریخچه تراکنش‌ها",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Filter Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            HistoryFilter.values().forEach { filter ->
                val isSelected = filter == activeFilter
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { viewModel.setHistoryFilter(filter) }
                        .border(
                            width = 1.dp,
                            color = if (isSelected) PrimaryDark else BorderColor,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    color = if (isSelected) PrimaryDark else White
                ) {
                    Text(
                        text = filter.title,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) White else TextDark
                    )
                }
            }
        }

        // List
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = TextMuted.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "هنوز تراکنشی ثبت نشده است",
                        fontSize = 13.5.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(historyList, key = { it.id }) { item ->
                    HistoryCard(
                        item = item,
                        onEdit = { viewModel.startEdit(it) },
                        onDelete = { viewModel.requestDelete(it) },
                        onShowReceipt = { viewModel.openReceiptForHistory(it) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
