package online.nyam.hikka.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.nyam.hikka.api.models.Genre

@Composable
fun GenreComposable(
    genre: Genre,
    modifier: Modifier = Modifier
) {
    AssistChip(
        {},
        { Text(genre.name) },
        modifier = modifier,
        border = null,
        shape = MaterialTheme.shapes.large,
        colors =
            AssistChipDefaults.assistChipColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.primary
            )
    )
}

@Composable
fun GenresVerticalComposable(
    genres: List<Genre>,
    modifier: Modifier = Modifier
) {
    Row(modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        genres.forEach {
            GenreComposable(it)
        }
    }
}
