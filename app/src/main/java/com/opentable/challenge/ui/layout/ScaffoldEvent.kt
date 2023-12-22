package com.opentable.challenge.ui.layout

sealed interface ScaffoldEvent {
    data object BackPressed : ScaffoldEvent
    data object AddPressed : ScaffoldEvent
}
