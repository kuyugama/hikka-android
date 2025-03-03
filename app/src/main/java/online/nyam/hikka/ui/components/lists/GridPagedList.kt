package online.nyam.hikka.ui.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : Any> VerticalPagedGrid(
    flow: Flow<PagingData<T>>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    loader: (@Composable () -> Unit)? = null,
    itemPlaceholder: (@Composable () -> Unit)? = null,
    itemContent: @Composable (T) -> Unit
) {
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    Column(modifier) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            loader?.invoke()
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey(key)) {
                val item = lazyPagingItems[it]

                if (item != null) {
                    itemContent(item)
                } else {
                    itemPlaceholder?.invoke()
                }
            }
            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) { loader?.invoke() }
            }
        }
    }
}
