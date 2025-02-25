package online.nyam.hikka.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Synonyms(
    synonyms: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Column(modifier.clickable { expanded = !expanded }) {
        Text("Інші назви", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
        val text = synonyms.joinToString("\n")
        CollapsibleText(
            content = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            expanded = expanded,
            collapsedLines = 2,
            animationDuration = 200
        )
    }
}
