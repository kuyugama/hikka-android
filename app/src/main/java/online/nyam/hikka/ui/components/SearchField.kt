package online.nyam.hikka.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
    onPromptUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var prompt by rememberSaveable {
        mutableStateOf("")
    }

    TextField(
        value = prompt,
        onValueChange = {
            prompt = it
            onPromptUpdate(it)
        },
        modifier =
            modifier.then(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ),
        singleLine = true,
        placeholder = { Text("Пошуковий запит") },
        shape = MaterialTheme.shapes.extraLarge,
        colors =
            TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    )
}
