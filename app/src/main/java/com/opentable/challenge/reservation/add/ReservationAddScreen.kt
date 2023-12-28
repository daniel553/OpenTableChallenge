package com.opentable.challenge.reservation.add

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.opentable.challenge.model.ReservationItem
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme

@Preview(showBackground = true)
@Composable
fun ReservationAddScreenPreview() {
    val state = ReservationAddFormState(
        reservation = ReservationItem(
            1L,
            "Name",
            System.currentTimeMillis()
        )
    )
    OpenTableChallengeTheme {
        ReservationAddScreen(state = state, onEvent = {})
    }
}

//💡The state will hold the actual the variants to show
@Composable
fun ReservationAddScreen(
    state: ReservationAddFormState,
    onEvent: (ReservationAddEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    ReservationAddFormView(
        state,
        onUpdated = { onEvent(ReservationAddEvent.OnUpdate(it)) },
        onSave = { onEvent(ReservationAddEvent.OnSave)},
        modifier = modifier
            .fillMaxSize()
            .testTag(ReservationAddScreenViewTag.ReservationAddForm.name)
    )
}

//💡To be used in finders of composable rule in instrumented test
enum class ReservationAddScreenViewTag {
    ReservationAddForm,
    ReservationAddLoading,
    ReservationAddButton
}