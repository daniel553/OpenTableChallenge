package com.opentable.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.opentable.challenge.ui.layout.AppScaffold
import com.opentable.challenge.ui.layout.ScaffoldEvent
import com.opentable.challenge.ui.nav.MainNavigation
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 💡Activity as hilt entry point, the intention is to have a single activity with navigation and
 * screens.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenTableChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold(scaffoldEvent = {
                        TODO("Handle event from scaffold")
                    }) {
                        MainNavigation()
                    }
                }
            }
        }
    }
}