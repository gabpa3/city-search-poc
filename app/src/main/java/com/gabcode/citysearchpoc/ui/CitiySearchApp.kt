package com.gabcode.citysearchpoc.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabcode.citysearchpoc.R
import com.gabcode.citysearchpoc.ui.home.HomeScreen
import com.gabcode.citysearchpoc.ui.map.MapScreen

@Composable
fun CitySearchApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<MainViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                modifier = modifier,
                viewModel = viewModel
            ) {
                navController.navigate(Screen.Map.route)
            }
        }
        composable(route = Screen.Map.route) {
            val result by viewModel.searchResult.collectAsState()
            MapScreen(cities = result)
        }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry_label))
            }
        }
    )
}
