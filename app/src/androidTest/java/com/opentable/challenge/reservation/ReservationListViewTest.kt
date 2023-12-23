package com.opentable.challenge.reservation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.opentable.challenge.model.ReservationItem
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ReservationListViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private var currentItem: ReservationItem? = null
    private val event: (ReservationItem) -> Unit = { currentItem = it }
    private val list = reservationItems(5)

    @Before
    fun setUp() {
        currentItem = null
        composeTestRule.setContent {
            ReservationListView(list, onSelect = { event(it) })
        }
    }

    @Test
    fun givenNonEmptyList_whenShowingReservationList_thenShowListOfReservations() {
        composeTestRule
            .onNodeWithTag(ReservationListViewTag.ReservationListViewLazyList.name)
            .assertExists()

        list.forEach { reservation ->
            composeTestRule.onNodeWithText(
                reservation.name,
                ignoreCase = true,
                useUnmergedTree = true
            ).assertExists()
        }
    }

    //💡Action event needs to be the same as it is passed to the view.
    @Test
    fun givenReservationItem_whenSelected_thenEventEmitsReservationItem() {
        val reservation = list.first()
        composeTestRule
            .onNodeWithTag(ReservationListViewTag.ReservationListItemView.name.plus(reservation.id))
            .performClick()

        Assert.assertEquals(currentItem, reservation)
    }
}