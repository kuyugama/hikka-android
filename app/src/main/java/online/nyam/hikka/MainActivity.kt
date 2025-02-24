package online.nyam.hikka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.launch
import online.nyam.hikka.api.API
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.responses.Manga
import online.nyam.hikka.api.paging.MangaPagingSource
import online.nyam.hikka.ui.components.MangaDetailsModal
import online.nyam.hikka.ui.components.MangaList
import online.nyam.hikka.ui.theme.HikkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HikkaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var showManga by remember { mutableStateOf<Manga?>(null) }

    val pager =
        remember {
            Pager(PagingConfig(15), initialKey = 2) {
                MangaPagingSource()
            }
        }

    if (showManga != null) {
        MangaDetailsModal(manga = showManga!!) {
            showManga = null
        }
    }

    MangaList(pager, modifier, onShowDetails = {
        scope.launch {
            when (val response = API.mangaDetails(it)) {
                is Response.Success -> showManga = response.data
                is Response.Error -> {}
            }
        }
    })
}
