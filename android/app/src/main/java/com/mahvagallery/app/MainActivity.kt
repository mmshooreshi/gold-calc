package com.mahvagallery.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.mahvagallery.app.ui.components.AppBottomBar
import com.mahvagallery.app.ui.components.ConfirmationDialog
import com.mahvagallery.app.ui.components.InfoDialog
import com.mahvagallery.app.ui.components.ReceiptDialog
import com.mahvagallery.app.ui.components.TopHeaderBar
import com.mahvagallery.app.ui.screens.CalculatorScreen
import com.mahvagallery.app.ui.screens.HistoryScreen
import com.mahvagallery.app.ui.screens.SettingsScreen
import com.mahvagallery.app.ui.screens.StatsScreen
import com.mahvagallery.app.ui.theme.MahvaGalleryTheme
import com.mahvagallery.app.viewmodel.AppTab
import com.mahvagallery.app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by viewModel.isDarkMode.collectAsState()
            val isBold by viewModel.isBoldText.collectAsState()
            val fontScaleDelta by viewModel.fontScaleDelta.collectAsState()
            val toastMsg by viewModel.toastMessage.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(toastMsg) {
                toastMsg?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            MahvaGalleryTheme(
                darkTheme = isDark,
                fontScaleDelta = fontScaleDelta,
                isBoldText = isBold
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MainAppScaffold(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeInfo by viewModel.activeInfoDialog.collectAsState()
    val activeReceipt by viewModel.activeReceiptDialog.collectAsState()
    val itemToDelete by viewModel.itemToDelete.collectAsState()
    val showClearModal by viewModel.showClearDataModal.collectAsState()

    Scaffold(
        topBar = { TopHeaderBar() },
        bottomBar = {
            AppBottomBar(
                currentTab = currentTab,
                onTabSelected = viewModel::selectTab
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Crossfade(targetState = currentTab, label = "tabTransition") { tab ->
                when (tab) {
                    AppTab.CALCULATOR -> CalculatorScreen(viewModel = viewModel)
                    AppTab.HISTORY -> HistoryScreen(viewModel = viewModel)
                    AppTab.STATS -> StatsScreen(viewModel = viewModel)
                    AppTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Modal Dialogs
    activeInfo?.let { info ->
        InfoDialog(
            title = info.title,
            description = info.description,
            onDismiss = viewModel::closeInfoDialog
        )
    }

    activeReceipt?.let { receipt ->
        ReceiptDialog(
            calcData = receipt.calcData,
            date = receipt.date,
            time = receipt.time,
            title = receipt.title,
            onDismiss = viewModel::closeReceiptDialog
        )
    }

    itemToDelete?.let {
        ConfirmationDialog(
            title = "حذف تراکنش؟",
            description = "این عمل قابل بازگشت نیست و تراکنش برای همیشه حذف می‌شود.",
            confirmText = "حذف",
            isDanger = true,
            onConfirm = viewModel::confirmDelete,
            onDismiss = viewModel::cancelDelete
        )
    }

    if (showClearModal) {
        ConfirmationDialog(
            title = "پاک‌سازی کامل داده‌ها",
            description = "تمام تراکنش‌ها، پیش‌فرض‌ها و تنظیمات ذخیره‌شده پاک خواهند شد. آیا مطمئن هستید؟",
            confirmText = "پاک‌سازی کامل",
            isDanger = true,
            onConfirm = viewModel::confirmClearAllData,
            onDismiss = viewModel::cancelClearAllData
        )
    }
}
