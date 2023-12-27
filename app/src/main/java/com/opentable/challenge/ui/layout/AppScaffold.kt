package com.opentable.challenge.ui.layout

import androidx.annotation.IdRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.opentable.challenge.R
import com.opentable.challenge.ui.nav.Router

/**
 * 💡Scaffold event is used to emit events when topbar or FAB are invoked, and
 * body to reuse the main app layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    state: AppScaffoldState,
    scaffoldEvent: (ScaffoldEvent) -> Unit,
    body: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(id = state.title.asResourceId()))
                },
                navigationIcon = {
                    if (state.showBack) {
                        IconButton(onClick = {
                            scaffoldEvent(ScaffoldEvent.BackPressed)
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!state.showBack) {
                FloatingActionButton(onClick = {
                    scaffoldEvent(ScaffoldEvent.AddPressed)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = { body() }
        )
    }
}

private fun String.asResourceId(): Int = when (this) {
    Router.ReservationScreen.destination -> R.string.nav_reservation
    Router.AddReservationScreen.destination -> R.string.nav_add_reservation
    else -> R.string.app_name
}
