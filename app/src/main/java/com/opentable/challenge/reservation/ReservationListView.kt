package com.opentable.challenge.reservation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.opentable.challenge.R
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
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (list.isEmpty()) {
        //💡empty list placeholder
        ReservationListEmptyView(
            onCallToAction = { onAdd() },
            modifier = modifier
                .fillMaxWidth()
                .testTag(ReservationListViewTag.ReservationListViewEmpty.name)
        )
    } else {
        LazyColumn(modifier = modifier.testTag(ReservationListViewTag.ReservationListViewLazyList.name)) {
            //💡 It's a good practice to define the key of the item
            items(list, key = { reservation -> reservation.id }) { reservation ->
                ReservationListItemView(
                    reservation = reservation,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onSelect(reservation)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationListViewPreview() {
    val list = reservationItemPreviewMock(5)
    OpenTableChallengeTheme {
        ReservationListView(list, {}, {})
    }
}

//💡Empty list placeholder when even a "success state" no items are presented
@Composable
fun ReservationListEmptyView(onCallToAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Outlined.DateRange, contentDescription = "reservations",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(84.dp)
                .height(84.dp)
                .padding(top = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.reservation_list_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.reservation_list_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Button(
            onClick = { onCallToAction() },
            modifier = Modifier
                .padding(8.dp)
                .testTag(ReservationListViewTag.ReservationListViewEmptyAction.name)
        ) {
            Text(text = stringResource(id = R.string.reservation_list_empty_action).uppercase())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationListEmptyViewPreview() {
    ReservationListEmptyView({})
}

//💡To be reused by the lazy column items
@Composable
fun ReservationListItemView(
    reservation: ReservationItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Intended to be empty
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .shadow(elevation = 4.dp, clip = false, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp)
            .testTag(ReservationListViewTag.ReservationListItemView.name.plus(reservation.id))
            .clickable { onClick() }) {
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
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primaryContainer,
        targetValue = MaterialTheme.colorScheme.inversePrimary,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )
    //💡The intention is to create like a skeleton of a list, but quite simple with animation
    LazyColumn(modifier) {
        items(4) {
            Box(
                modifier = Modifier
                    .height(62.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(1.dp)
                    .shadow(elevation = 4.dp, clip = false, shape = RoundedCornerShape(8.dp))
                    .drawBehind {
                        drawRect(color)
                    }
            )
        }
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning, contentDescription = "error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .width(84.dp)
                .height(84.dp)
                .padding(top = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.reservation_list_error_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.reservation_list_error_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationListErrorPreview() {
    OpenTableChallengeTheme {
        ReservationListError()
    }
}

enum class ReservationListViewTag {
    ReservationListViewLazyList,
    ReservationListItemView,
    ReservationListViewEmpty,
    ReservationListViewEmptyAction,
}
