package online.nyam.hikka.activities.main.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.nyam.hikka.activities.main.MangaRoute
import online.nyam.hikka.api.HikkaAPI
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.responses.Manga

data class MangaScreenState(
    val manga: Manga? = null
)

class MangaScreenViewModel(
    private val hikkaApi: HikkaAPI,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MangaScreenState())
    val state = _state.asStateFlow()

    init {
        val route = savedStateHandle.toRoute<MangaRoute>()
        viewModelScope.launch {
            when (val response = hikkaApi.mangaDetails(route.slug)) {
                is Response.Success -> {
                    _state.update { it.copy(manga = response.data) }
                }
                else -> {
                }
            }
        }
    }
}
