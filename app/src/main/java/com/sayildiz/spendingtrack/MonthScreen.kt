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
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.sayildiz.spendingtrack.model.Spend
import com.sayildiz.spendingtrack.model.SpendSummed
import com.sayildiz.spendingtrack.ui.theme.SpendingTrackTheme

@Composable
fun MonthScreen(
    viewModel: MonthViewModel = hiltViewModel()
) {
    var showAddSpendDialog by remember { mutableStateOf(false) }
    val spendList: List<Spend> = viewModel.spendItemsFlow.collectAsState().value
    val spendSummary: List<SpendSummed> = viewModel.spendSummedFlow.collectAsState().value
    Box(Modifier.fillMaxSize()) {
        SpendingList(Modifier, spendList, viewModel::deleteSpend)
        SpendingSummary(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 50.dp),
            spendSummary
        )
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
    spendList: List<Spend>,
    onClickDelete: (Spend) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxHeight(0.5f)) {
        items(spendList) {
            SpendingListItem(it, onClickDelete)
            HorizontalDivider()
        }
    }
    LazyColumn(modifier = modifier.fillMaxHeight(0.5f)) {
        items(spendList) {
            SpendingListItem(it, onClickDelete)
            HorizontalDivider()
        }
    }
}

@Composable
fun SpendingListItem(spend: Spend, onClickDelete: (spend: Spend) -> Unit) {
    ListItem(modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(spend.name)
                Text(color = Color.Magenta, text = spend.type)
            }
        },
        leadingContent = { Text(spend.amount.toString()) },
        trailingContent = {
            Button(onClick = { onClickDelete(spend) }) {
                Icon(Icons.Filled.Clear, "Delete SpendItem")
            }
        }
    )
}

@Composable
fun SpendingSummary(
    modifier: Modifier = Modifier,
    spendList: List<SpendSummed> = listOf(SpendSummed(type = "electronics", sum = 15.0)),
) {
    LazyColumn(modifier = modifier.fillMaxHeight(0.5f)) {
        items(spendList) {
            SpendingSummedItem(it)
            HorizontalDivider()
        }
    }
}

@Composable
fun SpendingSummedItem(summed: SpendSummed) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (summed.type != null) {
                    Text(color = Color.Magenta, text = summed.type)
                    Text(summed.sum.toString())
                }
            }
        },
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
    var name by rememberSaveable { mutableStateOf("") }
    var spendAmount by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    var isErrorName by rememberSaveable { mutableStateOf(false) }
    var isErrorSpendAmount by rememberSaveable { mutableStateOf(false) }
    var isErrorType by rememberSaveable { mutableStateOf(false) }

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
                    onValueChange = { name = it.trim(); isErrorName = false },
                    maxLines = 1,
                    singleLine = true,
                    label = { Text("Name") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    isError = isErrorName,
                    supportingText = { if (isErrorName) Text("Can't be empty") }

                )
                TextField(
                    value = spendAmount,
                    onValueChange = { newValue ->
                        isErrorSpendAmount = false
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
                    ),
                    isError = isErrorSpendAmount,
                    supportingText = { if (isErrorSpendAmount) Text("Can't be empty") }
                )
                TextField(
                    value = type,
                    onValueChange = { type = it.trim(); isErrorType = false },
                    maxLines = 1,
                    singleLine = true,
                    label = { Text("Type") },
                    isError = isErrorType,
                    supportingText = { if (isErrorType) Text("Can't be empty") }
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
                        if (name.isNotBlank() && spendAmount.isNotBlank() && type.isNotBlank()) {
                            onSave(Spend(name = name, amount = spendAmount.toDouble(), type = type))
                            onDismissRequest()
                        } else {
                            name.ifBlank { isErrorName = true }
                            spendAmount.ifBlank { isErrorSpendAmount = true }
                            type.ifBlank { isErrorType = true }
                        }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddSpendDialogPreview() {
    SpendingTrackTheme {
        AddSpendDialog({}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun SpendItemPreview() {
    SpendingTrackTheme {
        SpendingListItem(Spend(name = "Amazon", amount = 15.99, type = "electronics")) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SpendSummedPreview() {
    SpendingTrackTheme {
        SpendingSummedItem(SpendSummed(type = "electronics", sum = 15.99))
    }
}

@Preview(showBackground = true)
@Composable
fun MonthScreenPreview() {
    SpendingTrackTheme {
        var showAddSpendDialog by remember { mutableStateOf(false) }
        val spendList: List<Spend> =
            (1..10).map { Spend(name = "Amazon", amount = 15.0, type = "electronics") }.toList()
        val spendSummed: List<SpendSummed> = spendList.groupBy { it.type }
            .map { (type, spends) ->
                SpendSummed(
                    type = type,
                    sum = spends.sumOf { it.amount }
                )
            }
        Box(Modifier.fillMaxSize()) {
            SpendingList(Modifier, spendList) {}
            SpendingSummary(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 50.dp), spendSummed
            )
            AddSpendEntryButton(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) { showAddSpendDialog = true }
        }
        if (showAddSpendDialog) {
            AddSpendDialog({ showAddSpendDialog = false }, {})
        }
    }
}