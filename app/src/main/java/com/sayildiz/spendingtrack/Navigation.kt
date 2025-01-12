package com.sayildiz.spendingtrack

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sayildiz.spendingtrack.ui.theme.SpendingTrackTheme

class NavItem(val navigate: () -> Unit, val label: String)

@Composable
fun MainScreen(modifier: Modifier) {
    val navController = rememberNavController()
    val navItems = listOf(
        NavItem({ navController.navigate("month") }, "Month"),
        NavItem({ navController.navigate("year") }, "Year"),
        NavItem({ navController.navigate("stats") }, "Stats")
    )
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar() },
        bottomBar = {
            BottomAppBar {
                BottomNavigation(
                    navItems = navItems
                )
            }
        },
    ) { innerPadding ->
        Navigation(navController, modifier.padding(innerPadding))
    }
}

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "month",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("month") { MonthScreen() }
        composable("year") { YearScreen() }
        composable("stats") { StatsScreen() }
    }

}

@Composable
fun BottomNavigation(
    navItems: List<NavItem>
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.DateRange, Icons.Filled.Search)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.DateRange, Icons.Outlined.Search)
    NavigationBar {
        navItems.forEachIndexed { index, item ->
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
        MainScreen(Modifier)
    }
}
