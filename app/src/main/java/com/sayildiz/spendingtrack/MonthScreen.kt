package com.sayildiz.spendingtrack

import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sayildiz.spendingtrack.ui.theme.SpendingTrackTheme

@Composable
fun MonthScreen(modifier: Modifier) {
        SpendingList(modifier)
}

@Composable
fun SpendingList(modifier: Modifier) {
    val exampleMsg = listOf("item1", "item2", "item3", "item4", "item", "item6", "item7")
    LazyColumn(modifier = modifier.fillMaxHeight(0.4f)) {
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
fun AddSpendEntryButton(modifier: Modifier, onClick: () -> Unit){
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
fun MainScreenPreview() {            
    SpendingTrackTheme {
        MonthScreen(modifier = Modifier)
    }
}
