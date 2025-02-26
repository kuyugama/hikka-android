package online.nyam.hikka.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun Synonyms(
    synonyms: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier
            .clickable(
                remember {
                    MutableInteractionSource()
                },
                null
            ) { expanded = !expanded }
            .fillMaxWidth()
    ) {
        Text(
            "Інші назви",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        val colorScheme = MaterialTheme.colorScheme

        val content =
            rememberSaveable(colorScheme, saver = AnnotatedString.Saver) {
                buildAnnotatedString {
                    synonyms.forEachIndexed { ix, it ->
                        withStyle(SpanStyle(colorScheme.primary)) {
                            append("${ix + 1}. ")
                        }
                        withStyle(SpanStyle(colorScheme.secondary)) {
                            append(it)
                        }
                        if (ix != synonyms.lastIndex) {
                            append("\n")
                        }
                    }
                }
            }

        CollapsibleText(
            content = content,
            expanded = expanded,
            style = MaterialTheme.typography.bodyMedium,
            collapsedLines = 2,
            animationDuration = 200
        )
    }
}
