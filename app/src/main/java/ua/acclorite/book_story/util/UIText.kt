package ua.acclorite.book_story.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


sealed class UIText(val string: String?) {
    data class StringValue(val value: String) : UIText(value)
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : UIText(null)

    @Composable
    fun asString(): String {
        return when (this) {
            is StringValue -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringValue -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}