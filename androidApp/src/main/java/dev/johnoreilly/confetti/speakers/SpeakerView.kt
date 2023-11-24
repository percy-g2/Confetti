package dev.johnoreilly.confetti.speakers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import dev.johnoreilly.confetti.R
import dev.johnoreilly.confetti.decompose.SpeakersComponent
import dev.johnoreilly.confetti.decompose.SpeakersUiState
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.ui.ErrorView
import dev.johnoreilly.confetti.ui.HomeScaffold
import dev.johnoreilly.confetti.ui.LoadingView
import dev.johnoreilly.confetti.ui.isExpanded
import dev.johnoreilly.confetti.utils.plus


@Composable
fun SpeakersRoute(
    component: SpeakersComponent,
    windowSizeClass: WindowSizeClass,
    topBarActions: @Composable RowScope.() -> Unit,
) {
    val uiState by component.uiState.subscribeAsState()

    HomeScaffold(
        title = stringResource(R.string.speakers),
        windowSizeClass = windowSizeClass,
        topBarActions = topBarActions,
    ) {
        when (val uiState1 = uiState) {
            is SpeakersUiState.Success -> {
                if (windowSizeClass.isExpanded) {
                    SpeakerGridView(uiState1.speakers, component::onSpeakerClicked)
                } else {
                    SpeakerListView(uiState1.speakers, component::onSpeakerClicked)
                }
            }

            is SpeakersUiState.Loading -> LoadingView()
            is SpeakersUiState.Error -> ErrorView {

            }
        }
    }
}


@Composable
fun SpeakerGridView(
    speakers: List<SpeakerDetails>,
    navigateToSpeaker: (id: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        contentPadding = PaddingValues(16.dp).plus(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom).asPaddingValues()
        ),
        content = {
            items(speakers) { speaker ->
                Column(
                    modifier = Modifier
                        .clickable { navigateToSpeaker(speaker.id) }
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                            )
                        },
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )

                    Text(
                        text = speaker.name,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun SpeakerListView(
    speakers: List<SpeakerDetails>,
    navigateToSpeaker: (id: String) -> Unit
) {
    Column {
        if (speakers.isNotEmpty()) {
            LazyColumn {
                items(speakers) { speaker ->
                    SpeakerItemView(speaker, navigateToSpeaker)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun SpeakerItemView(
    speaker: SpeakerDetails,
    navigateToSpeaker: (id: String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { navigateToSpeaker(speaker.id) }),
        headlineContent = {
            Text(text = speaker.name)
        },
        supportingContent = speaker.tagline?.let { company ->
            {
                Text(company)
            }
        },
        leadingContent = {
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
                            .size(64.dp)
                            .clip(CircleShape),
                        colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                },
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
            )
        }
    )
}
