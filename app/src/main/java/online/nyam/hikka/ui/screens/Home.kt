package online.nyam.hikka.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.nyam.hikka.ui.components.MangaCard
import online.nyam.hikka.ui.components.MangaDetailsModal
import online.nyam.hikka.ui.components.SearchField
import online.nyam.hikka.ui.components.lists.VerticalPagedGrid
import online.nyam.hikka.ui.viewmodels.HomeScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    screenModel: HomeScreenViewModel = koinViewModel()
) {
    val screenState by screenModel.state.collectAsState()

    if (screenState.showDetails) {
        MangaDetailsModal(manga = screenState.mangaDetails!!) {
            screenModel.hideDetails()
        }
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SearchField(onPromptUpdate = screenModel::updateSearchQuery)

        VerticalPagedGrid(
            screenState.pager,
            { it.slug },
            loader = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        ) {
            MangaCard(
                manga = it,
                onShowDetails = {
                    screenModel.showDetailsOf(it.slug) { abort ->
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(abort.message)
                    }
                }
            )
        }
    }
}
