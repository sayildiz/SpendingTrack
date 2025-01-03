package com.sayildiz.spendingtrack

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sayildiz.spendingtrack.ui.theme.SpendingTrackTheme

@Composable
fun MonthScreen(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize()) {
        SpendingList(Modifier)
        AddSpendEntryButton(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) { }
    }
}


@Composable
fun SpendingList(modifier: Modifier = Modifier) {
    val exampleMsg = listOf("item1", "item2", "item3", "item4", "item", "item6", "item7")
    LazyColumn(modifier = modifier.fillMaxHeight(0.5f)) {
        items(exampleMsg) {
            SpendingListItem(it)
            HorizontalDivider()
        }
    }

}

@Composable
fun SpendingListItem(msg: String) {
    ListItem(
        headlineContent = { Text(msg) }
    )

}

@Composable
fun AddSpendEntryButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, "Floating Add Button")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("SpendingTrack")
        }
    )

}

@Preview
@Composable
fun MonthScreenPreview() {
    SpendingTrackTheme {
        MonthScreen(Modifier)
    }
}
