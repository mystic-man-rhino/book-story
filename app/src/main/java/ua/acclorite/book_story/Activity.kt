package ua.acclorite.book_story

import android.annotation.SuppressLint
import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.components.bottom_navigation_bar.BottomNavigationBar
import ua.acclorite.book_story.presentation.components.custom_navigation_rail.CustomNavigationRail
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.NavigationHost
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.browse.BrowseScreen
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.history.HistoryScreen
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.LibraryScreen
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.ReaderScreen
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.SettingsScreen
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.AppearanceSettings
import ua.acclorite.book_story.presentation.screens.settings.nested.general.GeneralSettings
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.ReaderSettings
import ua.acclorite.book_story.ui.BooksHistoryResurrectionTheme
import ua.acclorite.book_story.ui.DarkTheme
import ua.acclorite.book_story.ui.Theme
import ua.acclorite.book_story.ui.Transitions
import ua.acclorite.book_story.ui.isDark
import java.lang.reflect.Field


@SuppressLint("DiscouragedPrivateApi")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class Activity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val libraryViewModel: LibraryViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val browseViewModel: BrowseViewModel by viewModels()
    private val bookInfoViewModel: BookInfoViewModel by viewModels()
    private val readerViewModel: ReaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // New Cursor size for Room
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Initializing all variables
        mainViewModel.init(libraryViewModel)

        // Splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }

        // Edge to edge
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val windowClass = calculateWindowSizeClass(activity = this)
            val isLoaded by mainViewModel.isReady.collectAsState()

            val theme = mainViewModel.theme.collectAsState().value ?: Theme.BLUE
            val darkTheme =
                mainViewModel.darkTheme.collectAsState().value ?: DarkTheme.FOLLOW_SYSTEM
            val tabletUI = windowClass.widthSizeClass != WindowWidthSizeClass.Compact

            if (isLoaded) {
                BooksHistoryResurrectionTheme(
                    theme = theme,
                    isDark = darkTheme.isDark()
                ) {
                    NavigationHost(startScreen = Screen.LIBRARY) {
                        val currentScreen by this.getCurrentScreen().collectAsState()

                        AnimatedVisibility(
                            visible = currentScreen == Screen.LIBRARY ||
                                    currentScreen == Screen.HISTORY ||
                                    currentScreen == Screen.BROWSE,
                            enter = Transitions.BackSlidingTransitionIn,
                            exit = Transitions.SlidingTransitionOut
                        ) {
                            Scaffold(
                                bottomBar = {
                                    if (!tabletUI) {
                                        BottomNavigationBar(navigator = this@NavigationHost)
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            start = if (tabletUI) 80.dp else 0.dp,
                                            bottom = it.calculateBottomPadding()
                                        )
                                ) {
                                    composable(screen = Screen.LIBRARY) {
                                        @Suppress("UNCHECKED_CAST")
                                        LibraryScreen(
                                            viewModel = libraryViewModel,
                                            historyViewModel = historyViewModel,
                                            browseViewModel = browseViewModel,
                                            navigator = this@NavigationHost,
                                            addedBooks = retrieveArgument("added_books") as? List<Book>
                                                ?: emptyList()
                                        )
                                    }

                                    composable(screen = Screen.HISTORY) {
                                        HistoryScreen(
                                            viewModel = historyViewModel,
                                            libraryViewModel = libraryViewModel,
                                            navigator = this@NavigationHost
                                        )
                                    }

                                    composable(screen = Screen.BROWSE) {
                                        BrowseScreen(
                                            viewModel = browseViewModel,
                                            libraryViewModel = libraryViewModel,
                                            navigator = this@NavigationHost
                                        )
                                    }
                                }

                                if (tabletUI) {
                                    CustomNavigationRail(navigator = this@NavigationHost)
                                }
                            }
                        }

                        // Book Info
                        composable(
                            screen = Screen.BOOK_INFO,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            BookInfoScreen(
                                viewModel = bookInfoViewModel,
                                libraryViewModel = libraryViewModel,
                                browseViewModel = browseViewModel,
                                historyViewModel = historyViewModel,
                                navigator = this@NavigationHost
                            )
                        }
                        composable(
                            screen = Screen.READER,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            ReaderScreen(
                                viewModel = readerViewModel,
                                mainViewModel = mainViewModel,
                                libraryViewModel = libraryViewModel,
                                historyViewModel = historyViewModel,
                                navigator = this@NavigationHost
                            )
                        }

                        // Settings
                        composable(
                            screen = Screen.SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            SettingsScreen(
                                navigator = this@NavigationHost
                            )
                        }

                        // Nested categories
                        composable(
                            screen = Screen.GENERAL_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            GeneralSettings(
                                mainViewModel = mainViewModel,
                                navigator = this@NavigationHost
                            )
                        }
                        composable(
                            screen = Screen.APPEARANCE_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            AppearanceSettings(
                                mainViewModel = mainViewModel,
                                navigator = this@NavigationHost
                            )
                        }
                        composable(
                            screen = Screen.READER_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            ReaderSettings(
                                mainViewModel = mainViewModel,
                                navigator = this@NavigationHost
                            )
                        }

//                        Start screen (later)
//                        composable(screen = Screen.START) {
//                            StartScreen()
//                        }
                    }
                }
            }
        }
    }
}