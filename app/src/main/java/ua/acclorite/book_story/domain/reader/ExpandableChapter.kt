/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.reader

import androidx.compose.runtime.Immutable

@Immutable
data class ExpandableChapter(
    val parent: ReaderText.Chapter,
    val expanded: Boolean,
    val chapters: List<ReaderText.Chapter>?
)