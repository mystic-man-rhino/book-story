package ua.acclorite.book_story.presentation.screens.reader.components.app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.core.components.LocalHistoryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalLibraryViewModel
import ua.acclorite.book_story.presentation.core.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.core.util.removeDigits
import ua.acclorite.book_story.presentation.core.util.removeTrailingZero
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.ui.Colors

/**
 * Reader bottom bar. Has a slider to change progress.
 */
@Composable
fun ReaderBottomBar() {
    val state = LocalReaderViewModel.current.state
    val onEvent = LocalReaderViewModel.current.onEvent
    val onLibraryEvent = LocalLibraryViewModel.current.onEvent
    val onHistoryEvent = LocalHistoryViewModel.current.onEvent

    val progress by remember(state.value.book.progress) {
        derivedStateOf {
            (state.value.book.progress * 100)
                .toDouble()
                .removeDigits(4)
                .removeTrailingZero()
                .dropWhile { it == '-' } + "%"
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(Colors.readerSystemBarsColor)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {}
            )
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = progress,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(3.dp))
        Slider(
            value = state.value.book.progress,
            enabled = !state.value.lockMenu,
            onValueChange = {
                if (state.value.listState.layoutInfo.totalItemsCount > 0) {
                    onEvent(ReaderEvent.OnScroll(it))
                    onEvent(
                        ReaderEvent.OnChangeProgress(
                            progress = it,
                            firstVisibleItemIndex = state.value.listState.firstVisibleItemIndex,
                            firstVisibleItemOffset = 0,
                            refreshList = { book ->
                                onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                                onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                            }
                        )
                    )
                }
            },
            colors = SliderDefaults.colors(
                inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
                disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
                disabledThumbColor = MaterialTheme.colorScheme.primary,
                disabledInactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}