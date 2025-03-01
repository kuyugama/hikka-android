package online.nyam.hikka.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.nyam.hikka.api.API
import online.nyam.hikka.api.models.Abort
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.responses.Manga
import online.nyam.hikka.api.models.responses.MangaShort
import online.nyam.hikka.api.paging.MangaPagingSource

fun pagerFor(query: String?) = Pager(PagingConfig(15)) { MangaPagingSource(query) }

data class HomeState(
    val mangaDetails: Manga? = null,
    val showDetails: Boolean = false,
    val query: String? = null,
    val pager: Pager<Int, MangaShort> = pagerFor(null)
)

class HomeScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    /**
     * Updates search query and resets pager instance in state
     *
     * If query not changed - does nothing
     */
    fun updateSearchQuery(query: String?) {
        val formatted =
            query?.let {
                if (it.length > 2) it else null
            }

        if (_state.value.query != formatted) {
            _state.update {
                it.copy(
                    query = formatted,
                    pager = pagerFor(formatted)
                )
            }
        }
    }

    /**
     * Requests manga details from an API if it doesn't already set. If API returns abort response
     * it calls callback to properly display error in UI
     */
    fun showDetailsOf(
        slug: String,
        onAbort: (suspend (Abort) -> Unit)? = null
    ) {
        if (slug == _state.value.mangaDetails?.slug) {
            _state.update { it.copy(showDetails = true) }
            return
        }

        viewModelScope.launch {
            when (val response = API.mangaDetails(slug)) {
                is Response.Success -> {
                    _state.update {
                        it.copy(
                            mangaDetails = response.data,
                            showDetails = true
                        )
                    }
                }

                is Response.Error -> {
                    onAbort?.invoke(response.abort)
                }

                is Response.Crash -> {
                    onAbort?.invoke(
                        Abort(
                            "internal:crash",
                            response.error.message ?: response.error::class.simpleName!!
                        )
                    )
                }
            }
        }
    }

    /**
     * Sets showDetails state property to false
     */
    fun hideDetails() {
        _state.update { it.copy(showDetails = false) }
    }
}
