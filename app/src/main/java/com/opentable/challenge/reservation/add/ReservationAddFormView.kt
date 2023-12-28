package com.opentable.challenge.reservation.add

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.opentable.challenge.R
import com.opentable.challenge.model.ReservationItem
import com.opentable.challenge.ui.component.DropdownMenuItem
import com.opentable.challenge.ui.component.OutlinedTextWithExposedDropdownMenu
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme

@Preview(showBackground = true, heightDp = 300)
@Composable
fun ReservationAddFormViewPreview() {
    val state = ReservationAddFormState(
        ReservationItem(1L, "Name", 0L, timeString = "0:00 AM"),
        timeOptions = listOf(DropdownMenuItem("KEY", "TEXT"))
    )
    OpenTableChallengeTheme {
        ReservationAddFormView(state, {}, {}, modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ReservationAddFormView(
    state: ReservationAddFormState,
    onUpdated: (reservation: ReservationItem) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    //💡Let's use the constrain layout such as xml but in compose way,
    // but it actually this can be a regular column as long as this is quite simple view.
    // -> constraintLayout-compose artifact has a different versioning than Jetpack Compose.
    val notAvailable = stringResource(id = R.string.not_available)

    ConstraintLayout(modifier = modifier) {
        //💡Not sure how the reference naming conventions
        val (nameRef, timeRef, buttonRef) = createRefs()

        OutlinedTextField(
            state.reservation.name,
            label = { Text(text = stringResource(id = R.string.add_form_name)) },
            onValueChange = { onUpdated(state.reservation.copy(name = it)) },
            maxLines = 1,
            isError = state.errors.contains(ReservationAddFormError.NameRequired),
            modifier = Modifier
                .constrainAs(nameRef) {
                    top.linkTo(parent.top, margin = 16.dp) //💡margins!
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .widthIn(max = 300.dp)
                .fillMaxWidth()
        )

        OutlinedTextWithExposedDropdownMenu(
            label = { Text(text = stringResource(id = R.string.add_form_time)) },
            options = state.timeOptions,
            onSelected = { selection ->
                onUpdated(state.reservation.copy(timeString = selection.text))
            },
            isError = state.errors.contains(ReservationAddFormError.TimeRequired),
            optionDecoration = { item ->
                item?.text?.plus(if (item.enabled) "" else notAvailable) ?: ""
            },
            modifier = Modifier.constrainAs(timeRef) {
                top.linkTo(nameRef.bottom, margin = 8.dp)
                start.linkTo(nameRef.start)
                end.linkTo(nameRef.end)
                width = Dimension.fillToConstraints
            }
        )

        Button(
            onClick = { onSave() },
            enabled = state.errors.isEmpty() && state.reservation.name.isNotBlank(),
            modifier = Modifier
                .constrainAs(buttonRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(timeRef.start)
                    end.linkTo(timeRef.end)
                    width = Dimension.fillToConstraints //💡This fill to start and end constraint
                }
        ) {
            Text(text = stringResource(id = R.string.add_form_add_button).uppercase())
        }
    }
}