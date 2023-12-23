package com.opentable.challenge.reservation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.opentable.challenge.model.ReservationItem
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ReservationListScreenTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var state: ReservationListState
    private var currentEvent: ReservationListEvent? = null
    private val event: (ReservationListEvent) -> Unit = { currentEvent = it }

    @Test
    fun whenSuccessState_thenShowReservationListView() {
        state = ReservationListState.Success(list = reservationItems(5))
        setState()
        verifyExists(ReservationListScreenViewTag.ReservationListView.name)
    }

    @Test
    fun whenErrorState_thenShowErrorListView() {
        state = ReservationListState.Error
        setState()
        verifyExists(ReservationListScreenViewTag.ReservationListError.name)
    }

    @Test
    fun whenLoadingState_thenShowListShrimmerView() {
        state = ReservationListState.Loading
        setState()
        verifyExists(ReservationListScreenViewTag.ReservationListShrimmer.name)
    }

    /**
     * 💡why not using @Before?, the tests needs to re-assign state to compose rule and
     *   set content does not allow to do that in a simple way...
     */
    private fun setState() {
        composeTestRule.setContent {
            OpenTableChallengeTheme {
                ReservationListScreen(state = state, onEvent = event)
            }
        }
    }

    private fun verifyExists(tag: String) = composeTestRule.onNodeWithTag(tag).assertExists()


    private fun reservationItems(count: Int): List<ReservationItem> = (0 until count).map {
        ReservationItem(it.toLong(), "name$it", it.toLong(), timeString = "1:00 PM")
    }
}