package com.opentable.challenge.reservation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.opentable.challenge.model.ReservationItem
import com.opentable.challenge.model.reservationItemPreviewMock
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme

/**
 * 💡The reservation list view is like a RecyclerView in a compose way, so as a recycler view it
 * is good to define a "ViewHolder" that can be reused like down below.
 */
@Composable
fun ReservationListView(
    list: List<ReservationItem>,
    onSelect: (ReservationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        //💡 It's a good practice to define the key of the item
        items(list, key = { reservation -> reservation.id }) { reservation ->
            ReservationListItemView(reservation = reservation, modifier = Modifier.fillMaxWidth()) {
                onSelect(reservation)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationListViewPreview() {
    val list = reservationItemPreviewMock(5)
    OpenTableChallengeTheme {
        ReservationListView(list, {})
    }
}

//💡To be reused by the lazy column items
@Composable
fun ReservationListItemView(
    reservation: ReservationItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Intended to be empty
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Text(
            text = reservation.name.uppercase(),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = reservation.timeString,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun ReservationListShrimmer(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        //TODO("Not yet implemented")
        Text("Not yet implemented")
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationListShrimmerPreview() {
    OpenTableChallengeTheme {
        ReservationListShrimmer()
    }
}

@Composable
fun ReservationListError(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        //TODO("Not yet implemented")
        Text("Not yet implemented")
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationListErrorPreview() {
    OpenTableChallengeTheme {
        ReservationListError()
    }
}
