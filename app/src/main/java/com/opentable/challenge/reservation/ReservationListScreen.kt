package com.opentable.challenge.reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.opentable.challenge.model.reservationItemPreviewMock
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme

@Preview(showBackground = true)
@Composable
fun ReservationListScreenPreview() {
    val state = ReservationListState.Success(
        list = reservationItemPreviewMock(5)
    )
    OpenTableChallengeTheme {
        ReservationListScreen(state = state, onEvent = {})
    }
}

@Composable
fun ReservationListScreen(
    state: ReservationListState,
    onEvent: (ReservationListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        when (state) {
            ReservationListState.Error -> ReservationListError(
                modifier.testTag(ReservationListScreenViewTag.ReservationListError.name)
            )

            ReservationListState.Loading -> ReservationListShrimmer(
                modifier.testTag(ReservationListScreenViewTag.ReservationListShrimmer.name)
            )

            is ReservationListState.Success -> ReservationListView(
                state.list,
                onSelect = { onEvent(ReservationListEvent.onSelected(it)) },
                onAdd = { onEvent(ReservationListEvent.onAdd) },
                modifier = Modifier.testTag(ReservationListScreenViewTag.ReservationListView.name)
            )
        }
    }
}

enum class ReservationListScreenViewTag {
    ReservationListError,
    ReservationListShrimmer,
    ReservationListView
}