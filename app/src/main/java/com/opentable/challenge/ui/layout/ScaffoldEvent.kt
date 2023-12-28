package com.opentable.challenge.ui.layout

sealed interface ScaffoldEvent {
    data object BackPressed : ScaffoldEvent
    data object AddPressed : ScaffoldEvent
    data class OnDestinationChanged(
        val hasBackStack: Boolean,
        val destination: String
    ) : ScaffoldEvent
}

data class AppScaffoldState(
    val showBack: Boolean = false,
    val title: String = "",
    val snackBar: String? = ""
)
