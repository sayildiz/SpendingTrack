package com.sayildiz.spendingtrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.sayildiz.spendingtrack.model.Spend
import com.sayildiz.spendingtrack.ui.theme.SpendingTrackTheme

@Composable
fun MonthScreen(
    modifier: Modifier = Modifier,
    viewModel: MonthViewModel = hiltViewModel()
) {
    var showAddSpendDialog by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        SpendingList(Modifier, viewModel.itemsFlow.collectAsState().value)
        AddSpendEntryButton(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) { showAddSpendDialog = true }
    }
    if (showAddSpendDialog) {
        AddSpendDialog({ showAddSpendDialog = false }, viewModel::insertSpend)
    }
}


@Composable
fun SpendingList(
    modifier: Modifier = Modifier,
    spendList: List<Spend> = listOf(Spend(name = "Amazon", amount = 15.0))
) {
    LazyColumn(modifier = modifier.fillMaxHeight(0.5f)) {
        items(spendList) {
            SpendingListItem(it)
            HorizontalDivider()
        }
    }

}

@Composable
fun SpendingListItem(spend: Spend) {
    ListItem(
        headlineContent = { spend.name?.let { Text(it) } }
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


@Composable
fun AddSpendDialog(onDismissRequest: () -> Unit, onSave: (Spend) -> Unit) {
    var name by remember { mutableStateOf("") }
    var spendAmount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Add New Item",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    maxLines = 1,
                    singleLine = true,
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                TextField(
                    value = spendAmount,
                    onValueChange = { newValue ->
                        val isValid =
                            newValue.isEmpty() || newValue.matches("""\d*\.?\d{0,2}""".toRegex())
                        if (isValid) {
                            spendAmount = newValue
                        }
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )
                )
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    maxLines = 1,
                    singleLine = true,
                    label = { Text("Type") },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.fillMaxWidth(0.6f))
                Button(
                    modifier = Modifier.padding(end = 6.dp),
                    onClick = {
                        onSave(Spend(name = name, amount = spendAmount.toDouble(), type = type))
                        onDismissRequest()
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview
@Composable
fun AddSpendDialogPreview() {
    SpendingTrackTheme {
        AddSpendDialog({}, {})
    }
}

@Preview
@Composable
fun MonthScreenPreview() {
    SpendingTrackTheme {
        MonthScreen(Modifier)
    }
}
