@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.subcategories

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CategoryTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings.FontFamilySetting
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings.FontSizeSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings.FontStyleSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.settings.LetterSpacingSetting

/**
 * Font subcategory.
 * Contains all settings from Font.
 */
fun LazyListScope.FontSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.font_reader_settings) },
    showTitle: Boolean = true,
    topPadding: Dp,
    bottomPadding: Dp
) {
    item {
        if (showTitle) {
            CategoryTitle(
                title = title.invoke(),
                color = titleColor.invoke(),
                modifier = Modifier
                    .padding(
                        top = topPadding,
                        bottom = 8.dp
                    )
            )
        } else {
            Spacer(
                modifier = Modifier.height((topPadding - 8.dp).coerceAtLeast(0.dp))
            )
        }
    }

    item {
        FontFamilySetting()
    }

    item {
        FontStyleSetting()
    }

    item {
        FontSizeSetting()
    }

    item {
        LetterSpacingSetting()
    }

    item {
        Spacer(
            modifier = Modifier.height(bottomPadding)
        )
    }
}