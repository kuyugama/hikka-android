package online.nyam.hikka.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import online.nyam.hikka.api.HikkaAPI
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.responses.MangaShort
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MangaPagingSource(
    private val query: String? = null,
    private val pageSize: Int = 15
) : PagingSource<Int, MangaShort>(),
    KoinComponent {
    private val hikkaApi: HikkaAPI by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MangaShort> {
        val response = hikkaApi.mangaCatalog(query, page = params.key ?: 1, size = params.loadSize)
        return when (response) {
            is Response.Success -> {
                val data = response.data

                LoadResult.Page(
                    data.items,
                    if (data.info.page > 1) data.info.page - 1 else null,
                    if (data.info.page < data.info.totalPages) {
                        data.info.page + 1 + (params.loadSize / pageSize)
                    } else {
                        null
                    }
                )
            }

            is Response.Error -> LoadResult.Error(Throwable(response.abort.message))
            is Response.Crash -> LoadResult.Error(response.error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MangaShort>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.let { page ->
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }
}
