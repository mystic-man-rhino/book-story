/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.reader.images

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.images.components.ImagesAlignmentOption
import ua.acclorite.book_story.presentation.settings.reader.images.components.ImagesColorEffectsOption
import ua.acclorite.book_story.presentation.settings.reader.images.components.ImagesCornersRoundnessOption
import ua.acclorite.book_story.presentation.settings.reader.images.components.ImagesOption
import ua.acclorite.book_story.presentation.settings.reader.images.components.ImagesWidthOption

fun LazyListScope.ImagesSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.images_reader_settings) },
    showTitle: Boolean = true,
    showDivider: Boolean = true,
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider
    ) {
        item {
            ImagesOption()
        }

        item {
            ImagesColorEffectsOption()
        }

        item {
            ImagesCornersRoundnessOption()
        }

        item {
            ImagesAlignmentOption()
        }

        item {
            ImagesWidthOption()
        }
    }
}