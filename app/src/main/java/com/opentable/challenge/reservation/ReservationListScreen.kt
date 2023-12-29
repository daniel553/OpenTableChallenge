package com.opentable.challenge.reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    //💡Depending on state a state screen will be shown
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
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

//💡Used as part of ui tests for compose
enum class ReservationListScreenViewTag {
    ReservationListError,
    ReservationListShrimmer,
    ReservationListView
}