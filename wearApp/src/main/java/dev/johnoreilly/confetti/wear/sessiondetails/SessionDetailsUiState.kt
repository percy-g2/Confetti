package dev.johnoreilly.confetti.wear.sessiondetails

import dev.johnoreilly.confetti.fragment.SessionDetails
import dev.johnoreilly.confetti.navigation.SessionDetailsKey

sealed interface SessionDetailsUiState {
    object Error : SessionDetailsUiState
    object Loading : SessionDetailsUiState

    data class Success(
        val sessionId: SessionDetailsKey,
        val session: SessionDetails
    ) : SessionDetailsUiState
}