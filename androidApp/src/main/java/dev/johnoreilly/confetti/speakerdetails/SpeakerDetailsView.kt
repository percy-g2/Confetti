@file:OptIn(ExperimentalMaterial3Api::class)

package dev.johnoreilly.confetti.speakerdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import dev.johnoreilly.confetti.R
import dev.johnoreilly.confetti.decompose.SpeakerDetailsComponent
import dev.johnoreilly.confetti.decompose.SpeakerDetailsUiState
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.ui.ErrorView
import dev.johnoreilly.confetti.ui.LoadingView
import dev.johnoreilly.confetti.ui.component.ConfettiHeaderAndroid
import dev.johnoreilly.confetti.ui.component.SocialIcon


@Composable
internal fun SpeakerDetailsRoute(
    component: SpeakerDetailsComponent,
) {
    val uiState by component.uiState.subscribeAsState()

    when (val uiState1 = uiState) {
        is SpeakerDetailsUiState.Loading -> LoadingView()
        is SpeakerDetailsUiState.Error -> ErrorView()
        is SpeakerDetailsUiState.Success -> SpeakerDetailsView(
            uiState1.details,
            component::onSessionClicked,
            component::onCloseClicked,
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerDetailsView(
    speaker: SpeakerDetails,
    navigateToSession: (id: String) -> Unit,
    popBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SelectionContainer {
                        Text(
                            modifier = Modifier.padding(PaddingValues(start = 16.dp, end = 16.dp)),
                            text = speaker.name,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { popBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
    ) { innerPadding ->
        val contentPaddings = remember { PaddingValues(horizontal = 16.dp, vertical = 8.dp) }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(state = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .padding(contentPaddings),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    speaker.tagline?.let { city ->
                        Text(
                            text = city,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.size(16.dp))

                    SubcomposeAsyncImage(
                        model = speaker.photoUrl,
                        contentDescription = speaker.name,
                        loading = {
                            CircularProgressIndicator()
                        },
                        error = {
                            Image(
                                painter = painterResource(dev.johnoreilly.confetti.shared.R.drawable.ic_person_black_24dp),
                                contentDescription = speaker.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(240.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                            )
                        },
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )

                    Spacer(modifier = Modifier.size(24.dp))

                    speaker.bio?.let { bio ->
                        Text(
                            text = bio,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.size(16.dp))
                    }

                    Row(
                        Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        speaker.socials.forEach { socialsItem ->
                            SocialIcon(
                                modifier = Modifier.size(24.dp),
                                socialItem = socialsItem,
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(socialsItem.url))
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            SpeakerTalks(
                modifier = Modifier.padding(contentPaddings),
                sessions = speaker.sessions,
                navigateToSession = navigateToSession,
            )
        }
    }
}

@Composable
fun SpeakerTalks(
    sessions: List<SpeakerDetails.Session>,
    navigateToSession: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(Modifier.fillMaxWidth()) {

        ConfettiHeaderAndroid(icon = Icons.Filled.Event, text = stringResource(id = R.string.sessions))

        Spacer(modifier = Modifier.size(8.dp))

        Column(modifier) {
            sessions.forEach { session ->
                Row(
                    Modifier
                        .padding()
                        .fillMaxWidth()
                        .clickable {
                            navigateToSession(session.id)
                        }
                        .padding(vertical = 8.dp)) {
                    Text(session.title, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
