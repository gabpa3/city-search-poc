package com.gabcode.citysearchpoc.ui

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Map : Screen("map")
}
