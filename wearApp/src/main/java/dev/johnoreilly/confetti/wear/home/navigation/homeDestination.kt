@file:OptIn(ExperimentalHorologistComposeLayoutApi::class)

package dev.johnoreilly.confetti.wear.home.navigation

import androidx.navigation.NavGraphBuilder
import com.google.android.horologist.compose.navscaffold.ExperimentalHorologistComposeLayoutApi
import com.google.android.horologist.compose.navscaffold.scrollable
import dev.johnoreilly.confetti.wear.home.HomeRoute
import dev.johnoreilly.confetti.wear.navigation.ConfettiNavigationDestination
import kotlinx.datetime.LocalDate

object HomeDestination : ConfettiNavigationDestination {
    override val route = "home_route"
    override val destination = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    navigateToSession: (String) -> Unit,
    navigateToDay: (LocalDate) -> Unit,
    navigateToSettings: () -> Unit,
) {
    scrollable(route = HomeDestination.route) {
        HomeRoute(
            navigateToSession = navigateToSession,
            navigateToDay = navigateToDay,
            navigateToSettings = navigateToSettings,
            columnState = it.columnState
        )
    }
}