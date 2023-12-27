package com.opentable.challenge.util

import androidx.navigation.NavHostController

fun NavHostController.navigateDisctinct(destination: String) {
    if (currentDestination?.route != destination) {
        navigate(destination)
    }
}