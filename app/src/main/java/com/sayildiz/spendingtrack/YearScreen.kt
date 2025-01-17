package com.sayildiz.spendingtrack

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sayildiz.spendingtrack.model.SpendMonth
import java.time.ZoneId


@Composable
fun YearScreen(viewModel: YearViewModel = hiltViewModel()) {
    val monthList: List<SpendMonth> = viewModel.yearItemsFlow.collectAsState().value
    val monthMap = monthList.groupBy { it.date.atZone(ZoneId.systemDefault()).year }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(monthMap.keys.toList().sorted()) {
            Text("$it")
            monthMap[it]?.forEach { item ->
                Text("${item.date.atZone(ZoneId.systemDefault()).month}: ${item.amount}")
            }
        }

    }
}
