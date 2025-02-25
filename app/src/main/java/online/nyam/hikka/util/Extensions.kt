package online.nyam.hikka.util

import android.content.res.Resources
import androidx.compose.ui.unit.Dp

fun Int.toDp(): Dp =
    Dp(
        this /
            Resources.getSystem().displayMetrics.density
    )
