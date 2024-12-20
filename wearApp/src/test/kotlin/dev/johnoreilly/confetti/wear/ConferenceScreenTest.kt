@file:Suppress("UnstableApiUsage")

package dev.johnoreilly.confetti.wear

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTouchHeightIsEqualTo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import dev.johnoreilly.confetti.decompose.ConferencesComponent
import dev.johnoreilly.confetti.wear.conferences.ConferencesView
import dev.johnoreilly.confetti.wear.preview.TestFixtures.conferences
import dev.johnoreilly.confetti.wear.screenshots.BaseScreenshotTest
import dev.johnoreilly.confetti.wear.screenshots.WearDevice
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class ConferenceScreenTest(override val device: WearDevice) : BaseScreenshotTest() {
    init {
        tolerance = 0.03f
    }

    @Test
    fun conferencesScreen() {
        val columnState = TransformingLazyColumnState()

        composeRule.setContent {
            TestScaffold {
                ConferencesView(
                    columnState = columnState,
                    uiState = ConferencesComponent.Success(
                        conferences.groupBy { it.days.first().year }
                    ),
                    navigateToConference = {},
                )
            }
        }
        composeRule.onNodeWithText("KotlinConf 2023").assertIsDisplayed()
        takeScreenshot()

        columnState.requestScrollToItem(20, 0)
        takeScreenshot("_end")
    }

    @Test
    fun conferencesScreenA11y() {
        enableA11yTest()

        composeRule.setContent {
            TestScaffold {
                ConferencesView(
                    uiState = ConferencesComponent.Success(
                        conferences.groupBy { it.days.first().year }
                    ),
                    navigateToConference = {},
                )
            }
        }

        composeRule.onNodeWithText("KotlinConf 2023")
            .assertIsDisplayed()
            // TODO https://github.com/google/horologist/issues/2039
//                    .assertHasClickAction()
            .assertTouchHeightIsEqualTo(52.dp)

        takeScreenshot()

        scrollToBottom()
        takeScreenshot("_end")

        composeRule.onNodeWithText("Conferences")
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }
}