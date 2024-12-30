package com.sayildiz.spendingtrack

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun MainNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar() },
        bottomBar = {
            BottomAppBar {
                BottomNavigation(
                    navigateMonth = { navController.navigate("month") },
                    navigateYear = { navController.navigate("year") },
                    navigateStats = { navController.navigate("stats") })
            }
        },
        floatingActionButton = { AddSpendEntryButton(modifier) {} }

    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = "month"
        ) {
            composable("month") { MonthScreen(modifier = modifier) }
            composable("year") { YearScreen() }
            composable("stats") { StatsScreen() }
        }
    }

}

@Composable
fun BottomNavigation(
    navigateMonth: () -> Unit,
    navigateYear: () -> Unit,
    navigateStats: () -> Unit
) {
    class NavItem(val navigate: () -> Unit, val label: String)

    val items = listOf(
        NavItem({ navigateMonth() }, "Month"),
        NavItem({ navigateYear() }, "year"),
        NavItem({ navigateStats() }, "stats")
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.DateRange, Icons.Filled.Search)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.DateRange, Icons.Outlined.Search)
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(icon = {
                Icon(
                    if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                    contentDescription = item.label
                )
            },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    item.navigate()
                })
        }
    }
}

