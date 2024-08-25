package ua.acclorite.book_story.presentation.screens.browse.components.layout.grid

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.presentation.components.CustomCheckbox
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BrowseGridFileItem(file: SelectableFile, hasSelectedFiles: Boolean) {
    val lastModified = rememberSaveable {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(file.fileOrDirectory.lastModified()))
    }

    val sizeBytes = rememberSaveable { file.fileOrDirectory.length() }
    val fileSizeKB = rememberSaveable {
        if (sizeBytes > 0) sizeBytes.toDouble() / 1024.0 else 0.0
    }
    val fileSizeMB = rememberSaveable {
        if (sizeBytes > 0) fileSizeKB / 1024.0 else 0.0
    }
    val fileSize = rememberSaveable {
        if (fileSizeMB >= 1.0) "%.0f MB".format(fileSizeMB)
        else if (fileSizeMB > 0.0) "%.0f KB".format(fileSizeKB)
        else "0 KB"
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    if (file.isSelected) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                contentDescription = stringResource(id = R.string.file_icon_content_desc),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.primary
            )

            DefaultTransition(
                visible = hasSelectedFiles,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                CustomCheckbox(
                    selected = file.isSelected,
                    containerColor = MaterialTheme.colorScheme.surface,
                    size = 18.dp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            file.fileOrDirectory.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            lineHeight = 18.sp,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "$fileSize, $lastModified",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}